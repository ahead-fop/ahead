
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Benchmark', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Benchmark', :identifier => 'feature', , :ownerLayer => layer)

# Benchmark\Graph.jak

package_java_io = JavaM::Package.new(:name => 'java.io', :identifier => 'java.io', :isImported => true, :ownerLayer => layer)
feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
java_io_Reader = JavaM::Class.new(:name => 'Reader', :identifier => 'java.io.Reader', :owner => package_used)
java_lang_String = JavaM::Class.new(:name => 'String', :identifier => 'java.lang.String', :owner => package_used)
java_io_IOException = JavaM::Class.new(:name => 'IOException', :identifier => 'java.io.IOException', :owner => package_used)
feature_Graph.importsPackage << package_java_io

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_long = JavaM::PrimitiveType.new(:name =>'long', :identifier =>'long', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)

# Benchmark\Graph.jak

field_feature_Graph_inFile = JavaM::Field.new(:name => 'feature.Graph.inFile', :identifier => 'feature.Graph.inFile', :type => java_io_Reader, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Graph)
field_feature_Graph_ch = JavaM::Field.new(:name => 'feature.Graph.ch', :identifier => 'feature.Graph.ch', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => true, :isFinal => false, :ownerClass => feature_Graph)
field_feature_Graph_last = JavaM::Field.new(:name => 'feature.Graph.last', :identifier => 'feature.Graph.last', :type => primitiveType_long, :visibility => 'package', :nesting => 0, :isStatic => true, :isFinal => false, :ownerClass => feature_Graph, :value => '=0')
field_feature_Graph_current = JavaM::Field.new(:name => 'feature.Graph.current', :identifier => 'feature.Graph.current', :type => primitiveType_long, :visibility => 'package', :nesting => 0, :isStatic => true, :isFinal => false, :ownerClass => feature_Graph, :value => '=0')
field_feature_Graph_accum = JavaM::Field.new(:name => 'feature.Graph.accum', :identifier => 'feature.Graph.accum', :type => primitiveType_long, :visibility => 'package', :nesting => 0, :isStatic => true, :isFinal => false, :ownerClass => feature_Graph, :value => '=0')
method_feature_Graph_runBenchmark1String2 = JavaM::Method.new(:name => 'feature.Graph.runBenchmark(String)', :identifier => 'feature.Graph.runBenchmark(String)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
    {
        try {
            inFile = new FileReader( FileName );
        }
        catch ( IOException e )
        {
            System.out.println( \"Your file \" + FileName + \" cannot be read\" );
        }
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_runBenchmark1String2_FileName = JavaM::Parameter.new(:name => 'feature.Graph.runBenchmark(String).FileName', :identifier => 'feature.Graph.runBenchmark(String).FileName', :type => java_lang_String, :nesting => 0, :ownerMethod => method_feature_Graph_runBenchmark1String2)
method_feature_Graph_runBenchmark1String2.throws << java_io_IOException

method_feature_Graph_stopBenchmark12 = JavaM::Method.new(:name => 'feature.Graph.stopBenchmark()', :identifier => 'feature.Graph.stopBenchmark()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
    {
        inFile.close();
    }", :ownerClass => feature_Graph)
method_feature_Graph_stopBenchmark12.throws << java_io_IOException

method_feature_Graph_readNumber12 = JavaM::Method.new(:name => 'feature.Graph.readNumber()', :identifier => 'feature.Graph.readNumber()', :type => primitiveType_int, :nesting => 0, :visibility => 'public', :body => "
    {
        int index =0;
        char[] word = new char[80];
        int ch=0;
                
        ch = inFile.read();
        while( ch==32 )
            ch = inFile.read(); // skips extra whitespaces
                
        while( ch!=-1 && ch!=32 && ch!=10 ) // while it is not EOF, WS, NL
        {
            word[index++] = ( char )ch;
            ch = inFile.read();
        }
        word[index]=0;
                
        String theString = new String( word );
                
        theString = new String( theString.substring( 0,index ) );
        return Integer.parseInt( theString,10 );
    }", :ownerClass => feature_Graph)
method_feature_Graph_readNumber12.throws << java_io_IOException

method_feature_Graph_startProfile12 = JavaM::Method.new(:name => 'feature.Graph.startProfile()', :identifier => 'feature.Graph.startProfile()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        accum = 0;
        current = System.currentTimeMillis();
        last = current;
    }", :ownerClass => feature_Graph)

method_feature_Graph_stopProfile12 = JavaM::Method.new(:name => 'feature.Graph.stopProfile()', :identifier => 'feature.Graph.stopProfile()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        current = System.currentTimeMillis();
        accum = accum + ( current - last );
    }", :ownerClass => feature_Graph)

method_feature_Graph_resumeProfile12 = JavaM::Method.new(:name => 'feature.Graph.resumeProfile()', :identifier => 'feature.Graph.resumeProfile()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        current = System.currentTimeMillis();
        last = current;
    }", :ownerClass => feature_Graph)

method_feature_Graph_endProfile12 = JavaM::Method.new(:name => 'feature.Graph.endProfile()', :identifier => 'feature.Graph.endProfile()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        current = System.currentTimeMillis();
        accum = accum + ( current-last );
        System.out.println( \"Time elapsed: \" + accum + \" milliseconds\" );
    }", :ownerClass => feature_Graph)

