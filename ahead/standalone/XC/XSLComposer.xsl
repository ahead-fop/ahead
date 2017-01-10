<?xml version="1.0" encoding="utf-8"?>
<!-- XSLComposer 9 -->
<!-- Features: 
  * Tag composition policy implemented append, prepend, override 
  * Separator for tag composition
  * Before, after, replace refinements
  * Multiple refinements in one file 
  * Ready for manipulation with JDOM
  * Provides MetaTags for XSLT generation 
  * Preserves namespaces when copying attributes
  Tests:
  * Preserving namespaces when copying elements
-->
<!-- Last update: June 16, 2003 Time: 5pm -->
<!-- Generator of refinements based on refines specs -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	
  version="1.0">  
  <xsl:output method="xml" omit-xml-declaration="no" indent="yes"/>

  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/function">
   <stylesheetfunction>
    <xsl:apply-templates/>
   </stylesheetfunction>
  </xsl:template>


 <!-- Overrides processing instructions just in case -->
  <xsl:template match="processing-instruction()"/>	

  <!-- Matches the comments -->
  <xsl:template match="comment()">
   <xsl:copy-of select="."/>
  </xsl:template>

  <!-- Matches the rest of the tags -->
  <xsl:template match="*">
      <xsl:variable name="basename" select="name(.)"/>
      <xsl:element name="{$basename}">
       <!-- Fills up the corresponding attributes -->
       <xsl:for-each select="attribute::*">
         <xsl:variable name="attrname">      <!-- attribute name      -->
           <xsl:value-of select="name(.)"/> 
         </xsl:variable>
         <xsl:variable name="attrvalue">     <!-- attribute value     -->
           <xsl:value-of select="."/>
         </xsl:variable>
	 <xsl:variable name="attrnamespace"> <!-- attribute namespace -->
	   <xsl:value-of select="namespace-uri(.)"/>
         </xsl:variable>

	 <!-- creates the attribute -->
         <xsl:attribute name="{$attrname}" namespace="{$attrnamespace}"> 
           <xsl:value-of select="$attrvalue"/> 
   	 </xsl:attribute>

        </xsl:for-each>       

       <!-- Recursive calls -->
       <xsl:apply-templates/>

     </xsl:element>
  </xsl:template>

  <!-- Copies of the text element -->
  <xsl:template match="text()">
    <xsl:copy-of select="."/>
  </xsl:template>

  <!-- ////////////////////////////////////////////////////// -->
  <!-- Matches the refines tag -->
  <xsl:template match="/function/refine" xmlns:metaxsl="MetaXSL">

    <!-- Start Common -->	
      <metaxsl:stylesheet version="1.0" >
       <xsl:call-template name="common" />
    <!-- End Common   -->

    <!-- Creates the main template match for the XPath expression --> 
    <xsl:variable name="path">           <!-- path variable -->
      <xsl:value-of select="./@path"/> 
    </xsl:variable>	    

    <!-- Creates the template tag with its match patter-->
    <metaxsl:template match="{$path}">

    <!-- Gets the variables for tag composition tag and char -->
    <xsl:variable name="tagpolicy" select="./@tag"/>
    <xsl:variable name="separator" select="./@separator"/>
		 
    <!-- Creates the basename variable and initializes the element -->
    <!-- Auxiliary metavariable -->	
    <xsl:variable name="basename">{$basename}</xsl:variable>


     <metaxsl:variable name="basename" select="name(.)"/>
     <!--Create the element -->
     <metaxsl:element name="{$basename}">
 
     <!-- Insertion of the refinement attributes -->
     <xsl:for-each select="child::*/attribute::*">
	<!-- Auxiliary metavariable -->	
        <xsl:variable name="attribname" select="name(.)"/>
        <xsl:variable name="attribvalue" select="."/>
	<xsl:variable name="attribnamespace" select="namespace-uri(.)"/>

	<metaxsl:attribute name="{$attribname}" namespace="{$attribnamespace}">
	 <xsl:value-of select="$attribvalue"/>
        </metaxsl:attribute>
	

      </xsl:for-each>
    
    <!-- ======== Attribute composition ========= -->
      <metaxsl:for-each select="attribute::*">
      <metaxsl:variable name="attrname">
      <metaxsl:value-of select="name(.)"/>          
      </metaxsl:variable>
      <metaxsl:variable name="attrvalue"> 
      <metaxsl:value-of select="."/>
      </metaxsl:variable>
      <metaxsl:variable name="attrnamespace"> 
      <metaxsl:value-of select="namespace-uri(.)"/>
      </metaxsl:variable>
	      
      <!-- Checks to see if the base attributes are any of the  -->
      <!-- refinement attributes. If so append/prepend them     -->
      <!-- If not, simply include them                          -->
      <metaxsl:choose>         

     <!-- Metavariable for attribute name -->
    <xsl:variable name="attrname">{$attrname}</xsl:variable>	
    <xsl:variable name="attrnamespace">{$attrnamespace}</xsl:variable>
    <!-- <xsl:variable name="whenvalue">{$whenvalue}</xsl:variable> -->
 
    <!-- Creation of when options -->
    <xsl:for-each select="child::*/attribute::*">
      
	<xsl:variable name="whenvalue" select="name(.)"/>
        <metaxsl:when test="name(.) = '{$whenvalue}'">

        <metaxsl:attribute name="{$attrname}" namespace="{$attrnamespace}">

        <!-- ========= Tag composition policy ======= -->
	<!-- Selects appropiate tag policy -->
	<xsl:choose>

           <!-- Prepend policy -->
           <xsl:when test="$tagpolicy = 'prepend'">
             <xsl:value-of select="."/>	
	     <xsl:value-of select="$separator"/>	
             <metaxsl:value-of select="$attrvalue"/>
      	  </xsl:when>	

	   <!-- Override policy -->
           <xsl:when test="$tagpolicy = 'override'">		
               <xsl:value-of select="."/>
      	  </xsl:when>	

           <!-- Append policy is the default one -->
           <xsl:otherwise>		
               <metaxsl:value-of select="$attrvalue"/>
	       <xsl:value-of select="$separator"/>
               <xsl:value-of select="."/>
      	  </xsl:otherwise>		  
	  
	 </xsl:choose>
        <!-- End of Tag composition policy -->

      </metaxsl:attribute>
     </metaxsl:when>

    </xsl:for-each>
        
    <!-- Dummy case where there are no attributes, since at least one -->
    <!-- when test clause is present. This will always be false since -->
    <!-- the attribute name cannot be empty in a well-formed XML file -->
    <metaxsl:when test="name(.) = ''">
    </metaxsl:when>

    <!-- Creation of otherwise option -->
    <metaxsl:otherwise>
       <metaxsl:attribute name="{$attrname}" namespace="{$attrnamespace}">
         <metaxsl:value-of select="$attrvalue"/>
       </metaxsl:attribute>
    </metaxsl:otherwise>

    <!-- Creation of closing choose -->
    </metaxsl:choose>
  
     
    <!-- Creation of closing for-each -->
    </metaxsl:for-each>       
          
    <!-- Applies the templates to the rest of the inner tag of refines -->
    <!-- It requires two child to jump over the matched tag -->
    <xsl:apply-templates select="child::*/child::*"/>
      
    <!-- Creates the closing element tag -->
    </metaxsl:element>	
      
     <!-- Creates the closing template tag -->
     </metaxsl:template>

    <!-- Start Footer -->
       </metaxsl:stylesheet>	
    <!-- End Footer   -->    

    </xsl:template>  <!-- end of refines template -->

  <!-- ////////////////////////////////////////////////////// -->
  <!-- Matches the super tag -->
  <xsl:template match="super" xmlns:metaxsl="MetaXSL">
     <metaxsl:copy-of select="child::* | text() | comment()"/>
  </xsl:template>


  <!-- ////////////////////////////////////////////////////// -->
  <!-- Matches the after tag -->
  <xsl:template match="/function/after" xmlns:metaxsl="MetaXSL">

    <!-- Start Common -->	
      <metaxsl:stylesheet version="1.0" >
       <xsl:call-template name="common" />
    <!-- End Common   -->
 
	
    <!-- Creates the main template match for the XPath expression --> 
    <xsl:variable name="path">           <!-- path variable -->
      <xsl:value-of select="./@path"/> 
    </xsl:variable>	    

    <!-- Creates the template tag -->
    <metaxsl:template match="{$path}">

    <!-- Here goes the current tag matched -->
    <metaxsl:copy-of select="."/>

    <!-- The recursive calls copy the before code -->     
    <!-- Applies the transformation to the kids of before -->
    <xsl:apply-templates select="child::*|text()|comment()"/>

    <!-- Creates the closing template tag -->
    </metaxsl:template>
	
    <!-- Start Footer -->
       </metaxsl:stylesheet>	
    <!-- End Footer   -->    
 
  </xsl:template>


  <!-- ////////////////////////////////////////////////////// -->  
  <!-- Matches the replace tag -->
  <xsl:template match="/function/replace" xmlns:metaxsl="MetaXSL">

    <!-- Start Common -->	
      <metaxsl:stylesheet version="1.0" >
       <xsl:call-template name="common" />
    <!-- End Common   -->


    <!-- Creates the main template match for the XPath expression --> 
    <xsl:variable name="path">           <!-- path variable -->
      <xsl:value-of select="./@path"/> 
    </xsl:variable>	    

    <!-- Creates the template tag -->
    <metaxsl:template match="{$path}">

    <!-- The recursive calls copy the before code -->     
    <!-- Applies the transformation to the kids of before -->
    <xsl:apply-templates select="child::*|text()|comment()"/>

    <!-- Creates the closing template tag -->
     </metaxsl:template>

    <!-- Start Footer -->
       </metaxsl:stylesheet>	
    <!-- End Footer   -->    

  </xsl:template>

  <!-- ////////////////////////////////////////////////////// -->
  <!-- Matches the before tag -->
  <xsl:template match="/function/before" xmlns:metaxsl="MetaXSL">

    <!-- Start Common -->
      <metaxsl:stylesheet version="1.0" >
       <xsl:call-template name="common" />
    <!-- End Common   -->

    <!-- Creates the main template match for the XPath expression --> 

    <xsl:variable name="path">           <!-- path variable -->
      <xsl:value-of select="./@path"/> 
    </xsl:variable>	    

    <!-- Creates the template tag -->
    <metaxsl:template match="{$path}">
	
    <!-- The recursive calls copy the before code -->     
    <!-- Applies the transformation to the kids of before -->
    <xsl:apply-templates select="child::*|text()|comment()"/>

    <!-- Here goes the current tag matched -->
    <metaxsl:copy-of select="."/>

    <!-- Creates the closing template tag -->
     </metaxsl:template>

    <!-- Closes the stylesheet -->
    
    <!-- Start Footer -->
       </metaxsl:stylesheet>	
    <!-- End Footer   -->

  </xsl:template>

  <!-- //////////////////////////////////////////////////////// -->


  <!-- *********************************************************** -->
  <!-- Template for common part of generation of stylesheet -->
  <xsl:template name="common" xmlns:metaxsl="MetaXSL">

    <!-- Start Header -->

      <metaxsl:output method="xml" indent="yes"/> 

      <metaxsl:template match="/">
        <metaxsl:apply-templates/>
      </metaxsl:template>
      
      <!-- Overrides processing instructions just in case -->
      <metaxsl:template match="processing-instruction()"/>	
      
      <!-- Matches the comments -->
      <metaxsl:template match="comment()">
        <metaxsl:copy-of select="."/>
      </metaxsl:template>

      <!-- Matches the rest of the tags -->
      <!-- Auxiliary metavariable -->	
      <xsl:variable name="basename">{$basename}</xsl:variable>
      <xsl:variable name="attrname">{$attrname}</xsl:variable>
      <xsl:variable name="attrnamespace">{$attrnamespace}</xsl:variable>      

      <metaxsl:template match="*">
        <metaxsl:variable name="basename" select="name(.)"/>
        <metaxsl:element name="{$basename}">
      
        <!-- Fills up the corresponding attributes -->
        <metaxsl:for-each select="attribute::*">
          <metaxsl:variable name="attrname">   <!-- attribute name -->
             <metaxsl:value-of select="name(.)"/> 
          </metaxsl:variable>
          <metaxsl:variable name="attrvalue">  <!-- attribute value -->
             <metaxsl:value-of select="."/>
          </metaxsl:variable>
         <metaxsl:variable name="attrnamespace">  <!-- attribute namespace -->
             <metaxsl:value-of select="namespace-uri(.)"/>
          </metaxsl:variable>

	  <!-- creates attr -->
          <metaxsl:attribute name="{$attrname}" namespace="{$attrnamespace}"> 
             <metaxsl:value-of select="$attrvalue"/> 
          </metaxsl:attribute>
       </metaxsl:for-each>       
      
        <!-- Recursive calls -->
        <metaxsl:apply-templates/>
      
       </metaxsl:element>
      </metaxsl:template>
      
      <!-- Copies of the text element -->
      <metaxsl:template match="text()">
        <metaxsl:copy-of select="."/>
      </metaxsl:template>

    <!-- End Header   --> 
 
  </xsl:template>
  <!-- ////////////// End of Common ///////////// -->

</xsl:stylesheet>
