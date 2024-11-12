package org.example.entity.bookings;

import jakarta.xml.bind.annotation.*;
import org.example.entity.hotel.Hotel;
import org.example.entity.room.Room;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://example.com/hotel-booking/hotel}hotel"/&gt;
 *         &lt;element ref="{http://example.com/hotel-booking/room}room"/&gt;
 *         &lt;element name="clientName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="status"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="Підтверджено"/&gt;
 *               &lt;enumeration value="Скасовано"/&gt;
 *               &lt;enumeration value="В очікуванні"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="guestCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "hotel",
        "room",
        "clientName",
        "startDate",
        "endDate",
        "status",
        "guestCount"
})
public class Booking {

    @XmlElement(namespace = "http://example.com/hotel-booking/hotel", required = true)
    protected Hotel hotel;
    @XmlElement(namespace = "http://example.com/hotel-booking/room", required = true)
    protected Room room;
    @XmlElement(required = true)
    protected String clientName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(required = true)
    protected String status;
    protected Integer guestCount;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger id;

    /**
     * Gets the value of the hotel property.
     *
     * @return
     *     possible object is
     *     {@link Hotel }
     *
     */
    public Hotel getHotel() {
        return hotel;
    }

    /**
     * Sets the value of the hotel property.
     *
     * @param value
     *     allowed object is
     *     {@link Hotel }
     *
     */
    public void setHotel(Hotel value) {
        this.hotel = value;
    }

    /**
     * Gets the value of the room property.
     *
     * @return
     *     possible object is
     *     {@link Room }
     *
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the value of the room property.
     *
     * @param value
     *     allowed object is
     *     {@link Room }
     *
     */
    public void setRoom(Room value) {
        this.room = value;
    }

    /**
     * Gets the value of the clientName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Sets the value of the clientName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClientName(String value) {
        this.clientName = value;
    }

    /**
     * Gets the value of the startDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the guestCount property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getGuestCount() {
        return guestCount;
    }

    /**
     * Sets the value of the guestCount property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setGuestCount(Integer value) {
        this.guestCount = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

}
