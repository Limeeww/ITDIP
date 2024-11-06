<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:bk="http://example.com/hotel-booking/bookings"
                xmlns:ht="http://example.com/hotel-booking/hotel"
                xmlns:rm="http://example.com/hotel-booking/room"
                xmlns:rmType="http://example.com/hotel-booking/room-type"
                version="1.0">

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Bookings</title>
                <style>
                    /* Style definitions for the bookings table */
                    body { font-family: Arial, sans-serif; }
                    .wrapper { margin: 20px; }
                    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                    th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <div class="wrapper">
                    <h2>Список Бронювань</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>ID Бронювання</th>
                                <th>Ім'я Готелю</th>
                                <th>Місто</th>
                                <th>Тип Номера</th>
                                <th>Кількість Гостей</th>
                                <th>Статус</th>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:for-each select="bk:bookings/bk:booking">
                                <tr>
                                    <td><xsl:value-of select="@id"/></td>
                                    <td><xsl:value-of select="ht:hotel/ht:name"/></td>
                                    <td><xsl:value-of select="ht:hotel/ht:address/ht:city"/></td>
                                    <td><xsl:value-of select="rm:room/rm:type/rmType:typeName"/></td>
                                    <td><xsl:value-of select="bk:guestCount"/></td>
                                    <td><xsl:value-of select="bk:status"/></td>
                                </tr>
                            </xsl:for-each>
                        </tbody>
                    </table>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
