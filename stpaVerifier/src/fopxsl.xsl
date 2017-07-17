<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- author: Lukas Balzer -->
	<!-- STPA Verifier 2015/2016 -->
	
    <xsl:import href="templates.xsl"/>
	<xsl:param name="page.layout" select="A4"/>
     <xsl:param name="page.title" select="''"/>   
    <xsl:param name="table.head.size" select="14"/> 
    <xsl:param name="text.size" select="12"/>
    <xsl:param name="header.omit" select="false"/>      
    <xsl:param name="title.size" select="24"/> 
    <xsl:template match="/*">
    <fo:root>
        <!-- Page layout -->
	    <fo:layout-master-set>
			<xsl:call-template name="layout"/>
		</fo:layout-master-set>	
         	
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++ START OF PDF ++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
			
			<!-- +++++ Front-Page +++++ -->
			<fo:page-sequence >
         		<xsl:attribute name="master-reference"><xsl:value-of select="$page.layout"/></xsl:attribute>
				<fo:flow flow-name="xsl-region-body">
					<fo:block intrusion-displace="line">
						<fo:table space-after="30pt">
							<fo:table-column column-number="1" column-width="100%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell background-color="#1A277A" padding="10px"
										text-align="center">
										<xsl:call-template name="headTheme"/>
										<fo:block font-size="24pt" color="#FFFFFF">
											<xsl:call-template name="fontTheme"/>
												STPA Verifier-Report
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if test="logoPath">
									<fo:table-row>
										<fo:table-cell padding="15px" text-align="center">
											<fo:block>		
												<fo:external-graphic content-width="100%">
													<xsl:attribute name="src">
													<!-- Path of the logo via haz-file -->
													<xsl:value-of select="logoPath" />
													</xsl:attribute>
												</fo:external-graphic>
												</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<!-- Main-data for the front-page -->
					<fo:block intrusion-displace="line">
						<fo:table space-after="30pt" margin="5mm 5mm 10mm 5mm">
							<fo:table-column column-number="1" column-width="25%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="75%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											<xsl:value-of select="modelType" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:value-of select="modelName" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if test="modelCheckerVersion">
									<fo:table-row border="1pt solid black">
										<fo:table-cell padding="4px">
											<fo:block font-size="12pt" font-weight="bold">
												Model Checker:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="4px">
											<fo:block font-size="12pt">
												<xsl:value-of select="modelCheckerVersion" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											Date and Time
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:value-of select="date" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if test="company">
									<fo:table-row border="1pt solid black">
										<fo:table-cell padding="4px">
											<fo:block font-size="12pt" font-weight="bold">
												Company
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="4px">
											<fo:block font-size="12pt">
												<xsl:value-of select="company" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>

			<!-- +++++ Common Page content +++++ -->
			<fo:page-sequence white-space-collapse="true" id="total">  
         		<xsl:attribute name="master-reference"><xsl:value-of select="$page.layout"/></xsl:attribute>
            	
				<!-- Header-Block -->
				<fo:static-content flow-name="xsl-region-before">
						<xsl:call-template name="header">
                            <xsl:with-param name="pdfTitle" select="$page.title" />
						</xsl:call-template>
				</fo:static-content>

				<!-- Footer-Block -->
				<fo:static-content flow-name="xsl-region-after">
						<xsl:call-template name="footer"/>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<!-- *************** Results Table *************** -->
					<fo:block>
						<fo:block space-after="5pt" page-break-after="avoid">
                 			<xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
							Results Table
						</fo:block>
						<!-- Results-Table-Template -->
						<xsl:call-template name="results">
                            <xsl:with-param name="varSize" select="$text.size" />
                            <xsl:with-param name="headSize" select="$table.head.size" />
                            <xsl:with-param name="omitHeader" select="$header.omit" />
                           </xsl:call-template>
					</fo:block>
				
					<!-- *************** propertiesTable *************** -->
					<fo:block>
						<fo:block space-after="5pt" page-break-after="avoid">
                 			<xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
							Properties Table
						</fo:block>
						<!-- propertiesTable -->
						<xsl:call-template name="propertiesTable">
                            <xsl:with-param name="varSize" select="$text.size" />
                            <xsl:with-param name="headSize" select="$table.head.size" />
                            <xsl:with-param name="omitHeader" select="$header.omit" />
                           </xsl:call-template>
					</fo:block>

					<!-- *************** Counterexamples Table *************** -->
					<fo:block>
						<fo:block space-after="10pt" page-break-after="avoid">
                 			<xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
							Counterexamples
						</fo:block>
						<xsl:call-template name="counterexamples">
                            <xsl:with-param name="varSize" select="$text.size" />
                            <xsl:with-param name="headSize" select="$table.head.size" />
                            <xsl:with-param name="omitHeader" select="$header.omit" />
                           </xsl:call-template>
					</fo:block>

				</fo:flow>
			</fo:page-sequence>
			
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
			<!-- +++++++++++++++++++++ END OF PDF +++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->

		</fo:root>
	</xsl:template>
    


</xsl:stylesheet>

