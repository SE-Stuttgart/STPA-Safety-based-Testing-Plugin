<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    	<!-- ############################################################-->	
	<!-- ################### Templates of the PDF ###################-->
	<!-- ############################################################-->
	
	<!-- ################### Heading-Background-Color ################### -->
	<xsl:template name="headTheme">
		<xsl:if test="exportinformation/backgroundColor">
			<xsl:attribute name="background-color">
				<xsl:value-of select="exportinformation/backgroundColor" />
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	
	<!-- ################### Font-Color ################### -->
	<xsl:template name="fontTheme">
		<xsl:if test="exportinformation/fontColor">
			<xsl:attribute name="color">
				<xsl:value-of select="exportinformation/fontColor" />
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="hazardLinkColor">
		<xsl:choose>
			<xsl:when test="links = 'Not Hazardous'">
				<fo:block color="#2D7500">
					<xsl:value-of select="links" />
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block color="#820000">
					<xsl:value-of select="links" />
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- ################### Accidents-Table ################### -->
	<xsl:template name="accidentsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="5%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="50%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="15%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Title</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Related Hazards</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some accidents are defined -->
					<xsl:when test="hazacc/accidents/accident">
						<xsl:for-each select="hazacc/accidents/accident">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="links" />
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
	
	<!-- ################### Hazard-Table ################### -->
	<xsl:template name="hazardTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="5%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="4" column-width="15%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF" padding="3px">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Title</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Related Accidents</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="hazacc/hazards/hazard">
						<xsl:for-each select="hazacc/hazards/hazard">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="links" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no hazards defined the first row -->
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
	
	<!-- ################### Safety Constraints Table ################### -->
	<xsl:template name="safetyConstraintsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="5%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="65%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Safety Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some Safety Constraints are defined -->
					<xsl:when test="sds/safetyConstraints/safetyConstraint">
						<xsl:for-each select="sds/safetyConstraints/safetyConstraint">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no Safety Constraints defined the first row -->
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
	
	<!-- ################### System Goals Table ################### -->
	<xsl:template name="systemGoalsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="5%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="65%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">System Goal</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some System Goals are defined -->
					<xsl:when test="sds/systemGoals/systemGoal">
						<xsl:for-each select="sds/systemGoals/systemGoal">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no System Goals defined the first row -->
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
	
	<!-- ################### Design Requirements Table ################### -->
	<xsl:template name="designRequirementsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="5%"
		border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="65%"
			border-style="none" />
	
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
			<xsl:call-template name="headTheme" />
			<xsl:call-template name="fontTheme" />
			<fo:table-row>
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">No.</fo:block>
				</fo:table-cell>
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Design Requirement</fo:block>
				</fo:table-cell>
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Description</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
	
		<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
			<xsl:choose>
				<!-- Checks whether some Design Requirements are defined -->
				<xsl:when test="sds/designRequirements/designRequirement">
					<xsl:for-each select="sds/designRequirements/designRequirement">
						<fo:table-row border="none">
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
							</xsl:if>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="number" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="title" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="description" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</xsl:when>
				<!-- If there are no Design Requirements defined the first row -->
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
	
	<!-- ################### Control Actions Table ################### -->
	<xsl:template name="controlActionsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="5%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="65%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Control Action</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
			
            <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether there are some Control Actions defined -->
					<xsl:when test="cac/controlactions/controlaction">
						<xsl:for-each select="cac/controlactions/controlaction">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="description" />
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
	
	
	
	<!-- ################### Corresponding Safety Constraints Table ################### -->
	<xsl:template name="correspondingSafetyConstraintsTable">
	
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="50%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="50%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                     <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						   Unsafe Control Actions
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                                 <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						Corresponding Safety Constraints
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
				<xsl:choose>
					<!-- Checks for hazardous UCA -->
					<xsl:when
						test="cac/controlactions/controlaction/unsafecontrolactions/unsafecontrolaction/correspondingSafetyConstraint/text != ''">
						<xsl:for-each
							select="cac/controlactions/controlaction/unsafecontrolactions/unsafecontrolaction[links != 'Not Hazardous']">
	
								<fo:table-row border="none">
									<xsl:if test="position() mod 2 = 0">
										<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
									</xsl:if>
									<fo:table-cell padding="3px">
										<fo:block>
										   <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
											<xsl:value-of select="description" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
                                 <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
											<xsl:value-of select="correspondingSafetyConstraint/text" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>						
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no hazardous UCA defined the first row -->
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
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Causal Factors Table ################### -->
	<xsl:template name="causalFactorsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="80%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Component</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
							<fo:table border="none" space-after="30pt">
								<fo:table-column column-number="1" column-width="25%"
									border-style="none" />
								<fo:table-column column-number="2" column-width="25%"
									border-style="none" />
								<fo:table-column column-number="3" column-width="25%"
									border-style="none" />
								<fo:table-column column-number="4" column-width="25%"
									border-style="none" />
	
								<fo:table-header border="none" background-color="#1A277A"
									color="#FFFFFF">
									<!-- Sets the PDF-Theme-Color -->
									<xsl:call-template name="headTheme"/>
									<xsl:call-template name="fontTheme"/>
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-weight="bold">Causal Factor</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-weight="bold">Hazard Links</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-weight="bold">Safety Constraint</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-weight="bold">Notes</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body>
									<!-- Empty Table-Body -->
									<fo:table-row>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
	
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
				<!-- Checks if there are some components for the CausalFactors-Table -->
					<xsl:when test="controlstructure/component/children/component">
						<xsl:for-each select="controlstructure/component/children/component[componentType != 'TEXTFIELD']">
							<fo:table-row border-bottom="2pt solid black"
								border-top="2pt solid black">
								<fo:table-cell padding="4px" background-color="#FFFFFF"
									color="#000000" border-right="2pt solid black">
									<fo:block  font-weight="bold">
										<xsl:value-of select="text" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
								
									<!-- ***** CausalFactors with its relatives ***** -->
									<fo:block >
									<xsl:call-template name="causalFactorRelatives"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no CausalFactor-Components defined the first row -->
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
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Causal Factors-Sub-Table ################### -->
	<!-- Every Causal Factor and its relative can be found in this sub-table -->
	<xsl:template name="causalFactorRelatives">
    <fo:table>
			<fo:table-column column-number="1" column-width="25%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="25%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="25%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="25%"
				border-style="none" />
			<fo:table-body>
				<xsl:choose>
					<!-- Checks if there are some CausalFactors defined -->
					<!-- for a component. -->
					<xsl:when test="causalFactors/causalFactor">
					<xsl:for-each
					select="causalFactors/causalFactor">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
						</xsl:if>
						
						<!-- Causal Factor -->
						<fo:table-cell padding="3px">
							<fo:block>
								<xsl:value-of select="text" />
							</fo:block>
						</fo:table-cell>
						
						<!-- Hazard Links -->
						<fo:table-cell padding="3px">
							<!-- Decision of the Text-Color depending on the hazardous-state -->
							<xsl:call-template name="hazardLinkColor"/>
						</fo:table-cell>
						
						<!-- Safety Constraint -->
						<fo:table-cell padding="3px">
						<xsl:choose>
								<xsl:when test="safetyConstraint/text != ''">
									<fo:block>
										<xsl:value-of select="safetyConstraint/text"/>
									</fo:block>																					
								</xsl:when>
								<xsl:otherwise>
									<fo:block>
										&#x2014;
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>	
						</fo:table-cell>
						
						<!-- Notes or Rationale -->
						<fo:table-cell padding="3px">
							<xsl:choose>
								<xsl:when test="note = 'Note'">
									<fo:block>
										&#x2014;
									</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<fo:block>
										<xsl:value-of select="note" />
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>	
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
				</xsl:when>
				
				<xsl:otherwise>
				<fo:table-row>
				<fo:table-cell padding="3px">
			
							<fo:block>
								&#x2014;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="3px">
							<fo:block>
							&#x2014;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="3px">
							<fo:block>
							&#x2014;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="3px">
							<fo:block>
							&#x2014;
							</fo:block>
						</fo:table-cell>
				</fo:table-row>
				</xsl:otherwise>
				</xsl:choose>			
			</fo:table-body>
		</fo:table>
	</xsl:template>
<!-- ################### Unsafe Control Actions Table (UCA) ################### -->
	<xsl:template name="ucaTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/>       
    <xsl:param name="value"/>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="5" column-width="20%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" >
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Control Action</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Not providing causes hazard </fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Providing causes hazard </fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Wrong timing or order causes hazard </fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Stopped too soon or applied too long
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks if there are some Control Actions already defined -->
					<xsl:when test="cac/controlactions/controlaction">
						<xsl:for-each select="cac/controlactions/controlaction">
							<fo:table-row border-bottom="2pt solid black"
								border-top="2pt solid black">
								<fo:table-cell padding="4px" background-color="#FFFFFF"
									color="#000000" border-right="2pt solid black">
									<fo:block font-weight="bold">
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								
								<!-- Creates for each Control Action a Unsafe Control Action Cell -->
								
								<!-- NOT GIVEN-Cell -->
								<fo:table-cell padding="4px" background-color="#FFFFFF">
									<!-- NOT GIVEN - SubTable -->
									<xsl:call-template name="notGiven"/>
								</fo:table-cell>
			
								<!-- GIVEN INCORRECTLY-Cell -->
								<fo:table-cell padding="4px" background-color="#FFFFFF">
									<!-- GIVEN_INCORRECTLY - SubTable -->
									<xsl:call-template name="givenIncorrectly"/>
								</fo:table-cell>
			
								<!-- ########## WRONG TIMING -Cell ########## -->
								<fo:table-cell padding="4px" background-color="#FFFFFF">
									<!-- WRONG TIMING - SubTable -->
									<xsl:call-template name="wrongTiming"/>
								</fo:table-cell>
			
								<!-- ########## STOPPED TOO SOON-Cell ########## -->
								<fo:table-cell padding="4px">
									<!-- STOPPED TOO SOON - SubTable -->
									<xsl:call-template name="stoppedTooSoon"/>
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
	
	<!-- ################### UCA-Sub-Table- type: NOT GIVEN ################### -->
	<xsl:template name="notGiven">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>	
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each
					select="unsafecontrolactions/unsafecontrolaction[type='NOT_GIVEN']">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
						</xsl:if>
						<fo:table-cell padding="3px">
							<fo:block>
								<xsl:value-of select="description" />
								<!-- Chooses the Link-Colour depending on the hazardous-state -->
								<xsl:call-template name="ucaHazLinkColor"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: GIVEN INCORRECTLY ################### -->
	<xsl:template name="givenIncorrectly">
	<fo:table>
		<fo:table-column column-width="100%" />
		<!-- empty row -->
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block/>		
				</fo:table-cell>
			</fo:table-row>
			<!-- For each "GIVEN_INCORRECTLY"-UCA, a new row shall be generated -->
			<xsl:for-each
				select="unsafecontrolactions/unsafecontrolaction[type='GIVEN_INCORRECTLY']">
				<fo:table-row>
					<!-- Sets the row-colour -->
					<xsl:if test="position() mod 2 = 0">
						<xsl:attribute name="background-color">
				#D9D9D9
				</xsl:attribute>
					</xsl:if>
					<fo:table-cell padding="3px">
						<fo:block>
							<xsl:value-of select="description" />
							<!-- Chooses the Link-Colour depending on the hazardous-state -->
							<xsl:call-template name="ucaHazLinkColor"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: WRONG TIMING ################### -->
	<xsl:template name="wrongTiming">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>
	
						
					</fo:table-cell>
				</fo:table-row>
				<!-- For each "WRONG-TIMING"-UCA, a new row shall be generated -->
				<xsl:for-each
					select="unsafecontrolactions/unsafecontrolaction[type='WRONG_TIMING']">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
						</xsl:if>
						<fo:table-cell padding="3px">
							<fo:block>
								<xsl:value-of select="description" />
								<!-- Chooses the Link-Colour depending on the hazardous-state -->
								<xsl:call-template name="ucaHazLinkColor"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: STOPPED TOO SOON ################### -->
	<xsl:template name="stoppedTooSoon">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty Row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>
					</fo:table-cell>
				</fo:table-row>
				<!-- For each "STOPPED TOO SOON"-UCA, a new row shall be generated -->
				<xsl:for-each
					select="unsafecontrolactions/unsafecontrolaction[type='STOPPED_TOO_SOON']">
					<fo:table-row>
						<!-- Sets the row-colour -->
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
						#D9D9D9
						</xsl:attribute>
							</xsl:if>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="description" />
								<!-- Chooses the Link-Colour depending on the hazardous-state -->
								<xsl:call-template name="ucaHazLinkColor"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
	</xsl:template>
	
	<!-- ################### UCA - Hazardous-Color-Chooser ################### -->
	<xsl:template name="ucaHazLinkColor">
		<xsl:choose>
			<xsl:when test="links = 'Not Hazardous'">
				<fo:block color="#2D7500">
					&#x005B;
					<xsl:value-of select="links" />
					&#x005D;
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block color="#820000">
					&#x005B;
					<xsl:value-of select="links" />
					&#x005D;
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
      
</xsl:stylesheet>