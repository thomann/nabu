<?xml version="1.0" encoding="UTF-8" ?>

<!--
	Document   : fo.xsl
	Created on : 5. September 2004, 05:35
	Author     : pht
	Description:
	Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:output method="xml" />

	<xsl:template match="/vocabulary">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4t"
					page-height="21cm" page-width="29.7cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4t">
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template match="page">
		<fo:block keep-together.within-page="always">
			<xsl:if test="position() != last()"><xsl:attribute name="break-after">page</xsl:attribute></xsl:if>
			<fo:table width="29.7cm" height="21cm" table-layout="fixed">
				<fo:table-column column-number="1"
					column-width="proportional-column-width(1)"
					number-columns-repeated="{/vocabulary/@width}" />
				<fo:table-body>
					<xsl:apply-templates />
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>

	<xsl:template match="tr">
		<fo:table-row>
			<xsl:apply-templates />
		</fo:table-row>
	</xsl:template>

	<xsl:template match="td">
		<fo:table-cell border="solid 0.1px black"
			font-size="15pt" height="{/vocabulary/@td-height}" >
			<fo:block start-indent="{/vocabulary/@padding}" end-indent="{/vocabulary/@padding}" padding="{/vocabulary/@padding}">
				<fo:block font-weight="bold" font-size="80%" text-align="left">
					<xsl:attribute name="text-align"><xsl:choose><xsl:when test="../../@verso">right</xsl:when>
					<xsl:otherwise>left</xsl:otherwise></xsl:choose></xsl:attribute>
					<xsl:value-of select="@voc" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="@lesson"/>
				</fo:block>
				<xsl:apply-templates select="text|image" />
			</fo:block>
		</fo:table-cell>
	</xsl:template>

	<xsl:template match="empty">
		<fo:table-cell border="solid 0.1px black"
			height="{/vocabulary/@td-height}"/>
	</xsl:template>

	<xsl:template match="text">
		<fo:block text-align="center" font-size="20pt"
				font-family="{@font}, Gentium, Cyberbit, ArialUni">
			<xsl:value-of select="@text" />
		</fo:block>
	</xsl:template>
	
	<xsl:template match="image">
		<fo:block text-align="center">
			<fo:instream-foreign-object>
			<xsl:copy-of select="*" />
			</fo:instream-foreign-object>
		</fo:block>
	</xsl:template>

</xsl:stylesheet>
