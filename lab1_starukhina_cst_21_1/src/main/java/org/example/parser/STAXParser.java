package org.example.parser;

import org.example.entity.bookings.Booking;
import org.example.entity.bookings.Bookings;
import org.example.entity.hotel.Address;
import org.example.entity.hotel.Hotel;
import org.example.entity.room.Room;
import org.example.entity.room_type.RoomType;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class STAXParser {
    private static final boolean LOG_ENABLED = true;

    public static void log(Object o) {
        if (LOG_ENABLED) {
            System.out.println(o);
        }
    }

    private String currentElement;
    private Bookings bookings;
    private List<Booking> bookingList;
    private Booking booking;
    private Hotel hotel;
    private Address address;
    private Room room;
    private RoomType roomType;

    public Bookings getBookings() {
        return bookings;
    }

    private void handleStartElement(StartElement startElement) {
        String localName = startElement.getName().getLocalPart();
        currentElement = localName;
        log("Start Element: " + localName);

        switch (currentElement) {
            case "bookings" -> {
                bookings = new Bookings();
                bookingList = new ArrayList<>();
            }
            case "booking" -> {
                booking = new Booking();
                String bookingId = startElement.getAttributeByName(new QName("id")).getValue();
                booking.setId(new BigInteger(bookingId));
                log("Booking ID: " + bookingId);
            }
            case "hotel" -> {
                hotel = new Hotel();
            }
            case "address" -> {
                address = new Address();
            }
            case "room" -> {
                room = new Room();
            }
            case "type" -> {
                roomType = new RoomType();
            }
        }
    }

    private void handleCharacters(Characters characters) {
        String value = characters.getData().trim();
        if (value.isEmpty()) {
            return;
        }
        log("Characters: " + value);

        switch (currentElement) {
            case "hotelName" -> {
                hotel.setName(value);
            }
            case "street" -> {
                address.setStreet(value);
            }
            case "city" -> {
                address.setCity(value);
            }
            case "typeName" -> {
                roomType.setTypeName(value);
            }
            case "maxGuests" -> {
                roomType.setMaxGuests(Integer.parseInt(value));
            }
            case "number" -> {
                room.setNumber(Integer.parseInt(value));
            }
            case "price" -> {
                room.setPrice(new BigDecimal(value));
            }
            case "clientName" -> {
                booking.setClientName(value);
            }
            case "startDate" -> {
                try {
                    XMLGregorianCalendar startDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
                    booking.setStartDate(startDate);
                } catch (Exception e) {
                    throw new RuntimeException("Invalid start date format: " + value, e);
                }
            }
            case "endDate" -> {
                try {
                    XMLGregorianCalendar endDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
                    booking.setEndDate(endDate);
                } catch (Exception e) {
                    throw new RuntimeException("Invalid end date format: " + value, e);
                }
            }
            case "status" -> {
                booking.setStatus(value);
            }
            case "guestCount" -> {
                booking.setGuestCount(Integer.parseInt(value));
            }
        }
    }

    private void handleEndElement(EndElement endElement) {
        String localName = endElement.getName().getLocalPart();
        log("End Element: " + localName);

        switch (localName) {
            case "booking" -> {
                bookingList.add(booking);
            }
            case "bookings" -> {
                bookings.setBooking(bookingList);
            }
            case "hotel" -> {
                hotel.setAddress(address);
                booking.setHotel(hotel);
            }
            case "room" -> {
                room.setType(roomType);
                booking.setRoom(room);
            }
        }
    }

    public Bookings parseBookings(String xmlFilePath, String xsdFilePath) throws IOException, SAXException, XMLStreamException {
        validateXML(xmlFilePath, xsdFilePath);

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (FileInputStream inputStream = new FileInputStream(xmlFilePath)) {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(inputStream);

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    handleStartElement(event.asStartElement());
                } else if (event.isCharacters()) {
                    handleCharacters(event.asCharacters());
                } else if (event.isEndElement()) {
                    handleEndElement(event.asEndElement());
                }
            }
        }

        return bookings;
    }

    private void validateXML(String xmlFilePath, String xsdFilePath) throws IOException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsdFilePath));
        Validator validator = schema.newValidator();

        try (FileInputStream inputStream = new FileInputStream(xmlFilePath)) {
            validator.validate(new StreamSource(inputStream));
        } catch (SAXException | IOException e) {
            throw new SAXException("Validation failed: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        STAXParser parser = new STAXParser();
        System.out.println("===== STAX Parser =====");

        String xmlFilePath = Const.XML_FILE;
        String xsdFilePath = Const.XSD_FILE;

        Bookings bookings = parser.parseBookings(xmlFilePath, xsdFilePath);

        System.out.println("====================================");
        System.out.println(bookings);
        System.out.println("====================================");
    }
}