<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    	<!-- ############################################################-->	
	<!-- ################### Templates of the PDF ###################-->
	<!-- ############################################################-->
	
    <!-- Page layout -->
    <xsl:template name="layout">
    
		<!-- Page-Master for the common pages -->
		<fo:simple-page-master margin="5mm 5mm 5mm 5mm"
			page-height="297mm" page-width="210mm" master-name="titel">
			<fo:region-body margin="5mm 0mm 10mm 5mm" />
		</fo:simple-page-master>
		
		<fo:simple-page-master margin="5mm 5mm 5mm 5mm"
			page-width="auto" page-height="auto" master-name="auto">
			<fo:region-body margin="5mm 0mm 10mm 5mm" />
		</fo:simple-page-master>
			
		<fo:simple-page-master margin="5mm 5mm 10mm 5mm"
				page-height="210mm" page-width="297mm" master-name="A4Landscape">
				<fo:region-body margin="15mm 0mm 10mm 5mm" />
				<fo:region-before extent="20mm" display-align="before" />
				<fo:region-after extent="10mm" display-align="after" />
		</fo:simple-page-master>
				
		<fo:simple-page-master margin="5mm 5mm 10mm 5mm"
				page-height="297mm" page-width="210mm" master-name="A4">
				<fo:region-body margin="15mm 0mm 18mm 5mm" />
				<fo:region-before extent="20mm" display-align="before" />
				<fo:region-after extent="10mm" display-align="after" />
		</fo:simple-page-master>
	</xsl:template>	
	
	<!-- ################### Head ################### -->
	<xsl:template name="header">
      <xsl:param name="pdfTitle" select="NON"/> 
		<fo:table background-color="#DEDEDE">
			<fo:table-body>
			<fo:table-row>
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt">
							<xsl:if test="modelType">
								<xsl:value-of select="modelType" />
							</xsl:if>
						</fo:block>
					</fo:table-cell>
					
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt">
							<xsl:if test="modelCheckerVersion">
								Model Checker:
							</xsl:if>
						</fo:block>
					</fo:table-cell>
							
					
					
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt" text-align="right">
							<xsl:value-of select="date" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt">
							<xsl:value-of select="modelName" />
						</fo:block>
					</fo:table-cell>
					
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt">
							<xsl:if test="modelCheckerVersion">
								<xsl:value-of select="modelCheckerVersion" />
							</xsl:if>
						</fo:block>
					</fo:table-cell>
							
					
					
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt" text-align="right">

						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	
	<!-- ################### Footer ################### -->
	<xsl:template name="footer">
		<fo:table>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell background-color="#DEDEDE" padding="4px">
						<!-- Page Numbering -->
						<fo:block font-size="10pt" text-align="center">
							Page &#x0020;
							<fo:page-number />
							of
							<fo:page-number-citation-last ref-id="total"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell padding="2px">
						<fo:block font-size="8pt">Created with STPA Verifier</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Heading-Background-Color ################### -->
	<xsl:template name="headTheme">
		<xsl:if test="backgroundColor">
			<xsl:attribute name="background-color">
				<xsl:value-of select="backgroundColor" />
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	
	<!-- ################### Font-Color ################### -->
	<xsl:template name="fontTheme">
		<xsl:if test="fontColor">
			<xsl:attribute name="color">
				<xsl:value-of select="fontColor" />
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	

	
	<!-- ################### propertiesTable ################### -->
	<xsl:template name="propertiesTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="10%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="70%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="20%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Formula</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Status</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some accidents are defined -->
					<xsl:when test="safetyProperties/property">
						<xsl:for-each select="safetyProperties/property">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="id" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="formula" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="status" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no accidents defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	

		<!-- ################### results Table ################### -->
	<xsl:template name="results">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column border-style="solid" />
			<fo:table-column border-style="solid" />
			<fo:table-column border-style="solid" />
			<fo:table-column border-style="solid" />
			<fo:table-column border-style="solid" />
			<fo:table-column border-style="solid" />
			<fo:table-column border-style="solid" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">SSR</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">#Depth</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">#Stored States</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">#Transitions</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">#Time</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">#Memory Usage</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Result</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
			
            <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether there are some Control Actions defined -->
					<xsl:when test="verificationResults/result">
						<xsl:for-each select="verificationResults/result">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
										#D9D9D9
									</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="ssrLiteral" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="depth" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="storedStates" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="transitions" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="time" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="usedMemory" />
									</fo:block>
								</fo:table-cell>								
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="result" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no Control Actions defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
		<!-- ################### Counterexamples Template ################### -->
	<xsl:template name="counterexamples">
	  <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <xsl:if test="safetyProperties/property[counterexampleLines/line]">
		<xsl:for-each select="safetyProperties/property[counterexampleLines/line]">	
	      <fo:table border="none" space-after="30pt" >
	           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
				<fo:table-column border-style="none" />
				<fo:table-column border-style="none" />
				<fo:table-header border="none" background-color="#1A277A"
					color="#FFFFFF">
						<!-- Sets the PDF-Theme-Color -->
						<xsl:call-template name="headTheme"/>
						<xsl:call-template name="fontTheme"/>
	         			  <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-row>
						<fo:table-cell linefeed-treatment="preserve" wrap-option="wrap" padding="3px">
							<fo:block font-weight="bold">Counterexample</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="3px">
							<fo:block><xsl:value-of select="id" /></fo:block>									
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
			
				<fo:table-body>
	            <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
					
					<xsl:for-each select="counterexampleLines/line">	
						<fo:table-row border="none">
							<fo:table-cell number-columns-spanned="2" padding="3px">
								<fo:block>
									<xsl:value-of select="." />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		 </xsl:for-each>
	   </xsl:if>
      
      </xsl:template>
	
</xsl:stylesheet>