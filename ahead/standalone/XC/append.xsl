<?xml version="1.0" encoding="utf-8"?>
<!-- append3.xsl  -->
<!-- This XSLT file takes care of appending two function files. -->
<!-- Last update: June 23, 2003 -->
<!-- Features:
  * Preserves name spaces of the attributes copied
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">  
  <xsl:output method="xml" omit-xml-declaration="no" indent="yes"/>

  <xsl:template match="/" xmlns:metaxsl="MetaXSL">

    <!-- Stylesheet headers, overrides, and comments -->

    <metaxsl:stylesheet version="1.0">
    <metaxsl:output method="xml" indent="yes"/> 
    <metaxsl:template match="/">
        <metaxsl:apply-templates/>
    </metaxsl:template>
      
    <metaxsl:template match="processing-instruction()"/>	
      
    <metaxsl:template match="comment()">
        <metaxsl:copy-of select="."/>
    </metaxsl:template>


    <!-- Matches template /function & includes inner function -->
    <!-- Matches the /function template  -->
    <metaxsl:template match="/function">
	<!-- Creates the function attribute instead of the textual tag -->
        <!-- <function> -->
	<xsl:element name="function">
        <!-- Fills up the corresponding attributes -->
         <xsl:for-each select="/function/attribute::*">
           <xsl:variable name="attrname">       <!-- attribute name      -->
             <xsl:value-of select="name(.)"/> 
           </xsl:variable>
           <xsl:variable name="attrvalue">      <!-- attribute value     -->
             <xsl:value-of select="."/>
           </xsl:variable>
	   <xsl:variable name="attrnamespace">  <!-- attribute namespace -->
	     <xsl:value-of select="namespace-uri(.)"/>
           </xsl:variable>
	
	   <!-- creates the attribute -->
           <xsl:attribute name="{$attrname}" namespace="{$attrnamespace}">
             <xsl:value-of select="$attrvalue"/> 
   	   </xsl:attribute>
        </xsl:for-each>       

        <!-- here go the refinements of first (innermost) function -->
	<!-- Probably here it should be child not descendants -->
        <metaxsl:apply-templates select="descendant::*|comment()"/>

        <!-- This is the super that takes care of appending the second
             refinement at the end of the stylesheet  -->
    	<!-- Appends the contents of second function here  -->
    	<xsl:apply-templates select="child::*/child::*"/>     

    	<!-- Closes the function and template tags -->
    </xsl:element>
    <!--  </function> -->
    </metaxsl:template>
    
    <!-- Text for the matching of refine, before, after, replace & comments -->
    <!-- Copies the children of the function root for tags -->
    <!-- refine, before, after, replace -->
    <metaxsl:template match="/function/*">
	  <metaxsl:copy-of select="."/>
    </metaxsl:template>

    <!-- Copies of the text element -->
    <metaxsl:template match="text()"/>
      
    <!-- Closes the stylesheet tag -->
    </metaxsl:stylesheet>

  </xsl:template>


 <!-- Overrides processing instructions just in case -->
  <xsl:template match="processing-instruction()"/>	

  <!-- Matches the comments -->
  <xsl:template match="comment()">
   <xsl:copy-of select="."/>
  </xsl:template>

  <!-- Matches the rest of the tags. It is used to copy the contents of the -->
  <!-- rest of the tags including their attributes  -->
  <xsl:template match="*">
      <xsl:variable name="basename" select="name(.)"/>
      <xsl:element name="{$basename}">
       <!-- Fills up the corresponding attributes -->
       <xsl:for-each select="attribute::*">
         <xsl:variable name="attrname">       <!-- attribute name      -->
           <xsl:value-of select="name(.)"/> 
         </xsl:variable>
         <xsl:variable name="attrvalue">      <!-- attribute value     -->
           <xsl:value-of select="."/>
         </xsl:variable>
	 <xsl:variable name="attrnamespace">  <!-- attribute namespace -->
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

</xsl:stylesheet>
