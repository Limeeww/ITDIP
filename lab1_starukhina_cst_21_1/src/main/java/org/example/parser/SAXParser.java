package org.example.parser;

import org.example.entity.bookings.Booking;
import org.example.entity.bookings.Bookings;
import org.example.entity.hotel.Address;
import org.example.entity.hotel.Hotel;
import org.example.entity.room.Room;
import org.example.entity.room_type.RoomType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SAXParser extends DefaultHandler {

    private static final boolean LOG_ENABLED = false; // Логирование включено

    public static void log(Object o) {
        if (LOG_ENABLED) {
            System.out.println(o);
        }
    }

    private String xmlFilePath;
    private String xsdFilePath;
    private String curElement;
    private String parElement;
    private Bookings bookings;
    private List<Booking> bookingsList;
    private Booking booking;
    private Hotel hotel;
    private Address address;
    private Room room;
    private RoomType roomType;

    public Bookings getBookings() {
        return bookings;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        log("Parsing Error: " + exception.getMessage());
        System.err.println("====================================");
        System.err.println(xmlFilePath + " is NOT valid against " + xsdFilePath + ":\n" + exception.getMessage());
        System.err.println("====================================");
        throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        error(exception);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        curElement = localName;
        log("Start Element: " + localName);
        switch (curElement) {
            case "bookings" -> {
                bookings = new Bookings();
                bookingsList = new ArrayList<>();
                log("Initialized bookings and bookingsList.");
            }
            case "booking" -> {
                booking = new Booking();
                booking.setId(new BigInteger(attributes.getValue("id")));
                log("Initialized booking with id: " + booking.getId());
            }
            case "hotel" -> {
                hotel = new Hotel();
                log("Initialized hotel.");
            }
            case "address" -> {
                address = new Address();
                log("Initialized address.");
            }
            case "room" -> {
                room = new Room();
                log("Initialized room.");
            }
            case "type" -> {
                roomType = new RoomType();
                log("Initialized roomType.");
            }
            default -> parElement = curElement; // Запоминаем родительский элемент для дальнейшей обработки
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (value.isEmpty()) return;

        log("Processing characters for element: " + curElement + " with value: " + value);

        switch (curElement) {
            case "clientName" -> {
                booking.setClientName(value);
                log("Set clientName: " + value);
            }
            case "startDate" -> {
                booking.setStartDate(convertToXMLGregorianCalendar(value));
                log("Set startDate: " + value);
            }
            case "endDate" -> {
                booking.setEndDate(convertToXMLGregorianCalendar(value));
                log("Set endDate: " + value);
            }
            case "status" -> {
                booking.setStatus(value);
                log("Set status: " + value);
            }
            case "guestCount" -> {
                booking.setGuestCount(Integer.parseInt(value));
                log("Set guestCount: " + value);
            }
            case "hotelName" -> {  // Обрабатываем новое имя тега для отеля
                hotel.setName(value);
                log("Set hotel name: " + value);
            }
            case "typeName" -> {  // Обрабатываем новое имя тега для типа комнаты
                roomType.setTypeName(value);
                log("Set roomType name: " + value);
            }
            case "street" -> {
                address.setStreet(value);
                log("Set street: " + value);
            }
            case "city" -> {
                address.setCity(value);
                log("Set city: " + value);
            }
            case "maxGuests" -> {
                roomType.setMaxGuests(Integer.parseInt(value));
                log("Set maxGuests: " + value);
            }
            case "number" -> {
                room.setNumber(Integer.parseInt(value));
                log("Set room number: " + value);
            }
            case "price" -> {
                room.setPrice(new BigDecimal(value));
                log("Set price: " + value);
            }
        }
    }


    private XMLGregorianCalendar convertToXMLGregorianCalendar(String dateStr) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(dateStr);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error converting date to XMLGregorianCalendar", e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        log("End Element: " + localName);
        switch (localName) {
            case "bookings" -> {
                bookings.setBooking(bookingsList);
                log("Added bookingsList to bookings.");
            }
            case "booking" -> {
                bookingsList.add(booking);
                log("Added booking to bookingsList.");
            }
            case "hotel" -> {
                booking.setHotel(hotel);
                log("Set hotel for booking.");
            }
            case "address" -> {
                hotel.setAddress(address);
                log("Set address for hotel.");
            }
            case "room" -> {
                booking.setRoom(room);
                log("Set room for booking.");
            }
            case "type" -> {
                room.setType(roomType);
                log("Set roomType for room.");
            }
        }
        curElement = null; // Очищаем текущий элемент после завершения обработки
    }

    public Bookings parseBookings(String xmlFilePath, String xsdFilePath) throws SAXException, ParserConfigurationException, IOException {
        this.xmlFilePath = xmlFilePath;
        this.xsdFilePath = xsdFilePath;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsdFilePath));
        factory.setSchema(schema);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/validation/schema", false);

        javax.xml.parsers.SAXParser parser = factory.newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        parser.parse(new FileInputStream(xmlFilePath), this);

        return bookings;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SAXParser saxParser = new SAXParser();
        System.out.println("--== SAX Parser ==--");
        Bookings bookings = saxParser.parseBookings("src/main/resources/bookings.xml", "src/main/resources/bookings.xsd");
        System.out.println("====================================");
        System.out.println("Parsed Bookings: \n" + bookings);
        System.out.println("====================================");
    }
}
