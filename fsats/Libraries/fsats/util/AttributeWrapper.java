//
// AttributeWrapper
//
// $Revision: 1.1.1.1 $
//
// $Date: 2002-03-14 22:51:29 $
//
package fsats.util;

import java.lang.reflect.Method;

import fsats.util.debug.Assert;

/**
 * AttributeWrapper takes a source object and attribute name that correspond to 
 * get and set methods on the source object. It then allows set and get in a generic
 * manner using introspection.
 */
public class AttributeWrapper
{
    private Method getMethod;
    private Method setMethod;
    private Object source;
    private String attribName;
    private Class  paramType;
    
    /** 
     * Creates a new AttributeWrapper with the given arguments.
     *
     * @param source Object on which to make the method calls
     * @param attribName attribute name to which get and set will be prepended when
     * looking for corresponding methods
     * @param parameter type for the set method
     */
    public AttributeWrapper(Object source, String attribName, Class paramType)
    {
        Assert.isTrue(attribName != null);
        Assert.isTrue(paramType != null);

        this.source = source;
        this.paramType = paramType;
        this.attribName = Character.toUpperCase(attribName.charAt(0)) + 
            attribName.substring(1);
        
        initMethods();
    }
    
    /**
     * Sets a new source object. Should be the same class type as the current
     * source object. Null is also a valid value, however calls to setValue will
     * have no effect and calls to getValue will return null.
     *
     * @param source new source object
     */
    public void setSource(Object source)
    {
        Assert.isTrue(source == null || 
                      this.source == null || 
                      this.source.getClass().equals(source.getClass()));

        this.source = source;
        
        getMethod = null;
        setMethod = null;
        
        initMethods();
    }
    
    /**
     * Returns the current source object associated with this wrapper, or null
     * if no source is associated with the wrapper.
     *
     * @return current source object or null
     */
    public Object getSource()
    {
        return source;
    }
    
    /**
     * Calls the corresponding set method with the given value.
     *
     * @param value Object value to use for the set method call
     */
    public void setValue(Object value)
    {
        try
        {
            setMethod.invoke(source, new Object[] { value });
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * Calls the corresponding get method and returns the value. Null will be
     * returned if there is a problem. Note that null may also be returned as
     * a legitimate response from the get method call.
     *
     * @return Object from the get method call
     */
    public Object getValue()
    {
        try
        {
            return getMethod.invoke(source, null);
        }
        catch (Exception e)
        {
        }
        
        return null;
    }
    
    private void initMethods()
    {        
        if (source == null)
        {
            return;
        }
        
        Class sourceClass = source.getClass();
        
        try
        {
            if (paramType.equals(boolean.class))
            {
                // Try isMethod first if we have a boolean type
                try
                {
                    getMethod = sourceClass.getMethod("is" + attribName, null);
                }
                catch (NoSuchMethodException nsme)
                {
                    // fall through and try to grab a get method
                }
            }
            if (getMethod == null)
            {
                getMethod = sourceClass.getMethod("get" + attribName, null);
            }
            setMethod = sourceClass.getMethod("set" + attribName,
                                               new Class[] { paramType });
        }
        catch (NoSuchMethodException nsme)
        {
            Assert.isTrue(false);
        }
        catch (SecurityException se)
        {
            Assert.isTrue(false);
        }
    }

    public static class TestClass
    {
        private int venus;
        private String fun;
        private boolean window;
        
        public void setVenus(int venus)
        {
            this.venus = venus;
        }
        
        public int getVenus()
        {
            return venus;
        }
        
        public void setFun(String fun)
        {
            this.fun = fun;
        }
        
        public String getFun()
        {
            return fun;
        }
        
        public void setWindow(boolean window)
        {
            this.window = window;
        }
        
        public boolean isWindow()
        {
            return window;
        }
    }
    
    public static void main(String[] args)
    {
        TestClass tc = new TestClass();
        
        AttributeWrapper venusWrap = new AttributeWrapper(tc, "Venus", int.class);
        AttributeWrapper funWrap = new AttributeWrapper(tc, "fun", String.class);
        AttributeWrapper windowWrap = new AttributeWrapper(tc, "window", boolean.class);
        
        System.out.println("[Pre Set] Venus value: " + venusWrap.getValue());
        System.out.println("[Pre Set] Fun value: " + funWrap.getValue());
        System.out.println("[Pre Set] Window value: " + windowWrap.getValue());
        
        venusWrap.setValue(new Integer(66));
        funWrap.setValue("This is fun!");
        windowWrap.setValue(new Boolean(true));

        System.out.println();        
        System.out.println("[Post Set] Venus value: " + venusWrap.getValue());
        System.out.println("[Post Set] Fun value: " + funWrap.getValue());
        System.out.println("[Post Set] Window value: " + windowWrap.getValue());
        
        TestClass tc2 = new TestClass();
        funWrap.setSource(tc2);
        windowWrap.setSource(null);
        
        System.out.println(); 
        System.out.println("[Pre Set2] Fun value: " + funWrap.getValue());
        System.out.println("[Pre Set2] Window value (source null): " +
                           windowWrap.getValue());
                           
        funWrap.setValue("We have some fun");
        windowWrap.setValue(new Boolean(true));
        
        System.out.println("[Post Set2] Fun value: " + funWrap.getValue());
        System.out.println("[Post Set2] Window value: " + windowWrap.getValue());
        
        System.out.println();
        System.out.println("The following should be an Assert exception to test a bad case");
        
        Object badAndy = new Object();
        funWrap.setSource(badAndy);
    }
}
