<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:p="http://javaops.ru"
                exclude-result-prefixes="p">
    <xsl:output method="html"/>
    <xsl:param name="project-name"/>
    <xsl:template match="/">
        <html>
            <body>
                <h1>User Details</h1>
                <table border="1">
                    <tr>
                        <th>Project</th>
                        <th>Group</th>
                    </tr>
                    <xsl:apply-templates select="/p:Payload/p:Projects/p:Project[p:name=$project-name]"/>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="p:Project">
        <tr>
            <td>
                <xsl:value-of select="p:name"/>
            </td>
            <td>
                <xsl:apply-templates select="p:Groups"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="p:Group">
        <div>
            <xsl:value-of select="@id"/>
        </div>
    </xsl:template>
</xsl:stylesheet>