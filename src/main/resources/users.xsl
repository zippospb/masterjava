<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="/*[name()='Payload']/*[name()='Users']">
        <html>
            <body>
                <h1>User Details</h1>
                <table border="1">
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                    </tr>
                    <xsl:for-each select="//*[name()='User']">
                        <tr>
                            <td>
                                <xsl:value-of select="*[name()='fullName']/."/>
                            </td>
                            <td>
                                <xsl:value-of select="@email"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>