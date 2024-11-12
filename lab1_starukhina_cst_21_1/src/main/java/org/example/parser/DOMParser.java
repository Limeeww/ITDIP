package org.example.parser;

import org.example.entity.bookings.Bookings;
import org.example.entity.bookings.Booking;
import org.example.entity.hotel.Hotel;
import org.example.entity.hotel.Address;
import org.example.entity.room.Room;
import org.example.entity.room_type.RoomType;
import org.w3c.dom.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;

public class DOMParser {

    private static boolean logEnabled = false;

    private static void log(Object o) {
        if (logEnabled) {
            System.out.println(o);
        }
    }

    public Bookings unmarshallBookings(String xmlFilePath, String xsdFilePath) throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsdFilePath));

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setSchema(schema);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.err.println("====================================");
                System.err.println(xmlFilePath + " is NOT valid against " + xsdFilePath + ":\n" + exception.getMessage());
                System.err.println("====================================");
                throw exception;
            }
        });

        Document document = documentBuilder.parse(new FileInputStream(xmlFilePath));
        Bookings bookings = new Bookings();
        List<Booking> bookingList = new ArrayList<>();
        Element root = document.getDocumentElement();
        NodeList xmlBookings = root.getElementsByTagNameNS("http://example.com/hotel-booking/bookings", "booking");

        for (int i = 0; i < xmlBookings.getLength(); i++) {
            bookingList.add(parseBooking((Element) xmlBookings.item(i)));
        }
        bookings.setBooking(bookingList);
        return bookings;
    }

    private Booking parseBooking(Element bookingElement) throws Exception {
        Booking booking = new Booking();
        booking.setId(new BigInteger(bookingElement.getAttribute("id")));
        log("Booking ID: " + booking.getId());

        NodeList nodes = bookingElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            String localName = item.getLocalName();
            log("Processing node: " + localName);

            if ("hotel".equals(localName)) {
                booking.setHotel(parseHotel(item));
            } else if ("room".equals(localName)) {
                booking.setRoom(parseRoom(item));
            } else if ("clientName".equals(localName)) {
                booking.setClientName(item.getTextContent());
                log("Client Name: " + booking.getClientName());
            } else if ("startDate".equals(localName)) {
                booking.setStartDate(toXMLGregorianCalendar(item.getTextContent()));
                log("Start Date: " + booking.getStartDate());
            } else if ("endDate".equals(localName)) {
                booking.setEndDate(toXMLGregorianCalendar(item.getTextContent()));
                log("End Date: " + booking.getEndDate());
            } else if ("status".equals(localName)) {
                booking.setStatus(item.getTextContent());
                log("Status: " + booking.getStatus());
            } else if ("guestCount".equals(localName)) {
                String guestCountText = item.getTextContent();
                booking.setGuestCount(guestCountText != null ? Integer.parseInt(guestCountText) : null);
                log("Guest Count: " + booking.getGuestCount());
            }
        }
        return booking;
    }

    private XMLGregorianCalendar toXMLGregorianCalendar(String date) throws DatatypeConfigurationException {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
    }

    private Hotel parseHotel(Node node) {
        Hotel hotel = new Hotel();
        NodeList nodes = node.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            log("Parsing node: " + item.getLocalName());

            if ("hotelName".equals(item.getLocalName())) {
                log("Hotel name found: " + item.getTextContent());
                hotel.setName(item.getTextContent());
            } else if ("address".equals(item.getLocalName())) {
                Address address = new Address();
                NodeList addressNodes = item.getChildNodes();

                for (int j = 0; j < addressNodes.getLength(); j++) {
                    Node addressItem = addressNodes.item(j);
                    if ("street".equals(addressItem.getLocalName())) {
                        log("Street found: " + addressItem.getTextContent());
                        address.setStreet(addressItem.getTextContent());
                    } else if ("city".equals(addressItem.getLocalName())) {
                        log("City found: " + addressItem.getTextContent());
                        address.setCity(addressItem.getTextContent());
                    }
                }
                hotel.setAddress(address);
            }
        }
        return hotel;
    }

    private Room parseRoom(Node roomNode) {
        Room room = new Room();
        NodeList nodes = roomNode.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            log("Processing node: " + item.getLocalName());

            if ("type".equals(item.getLocalName())) {
                RoomType roomType = new RoomType();
                NodeList typeNodes = item.getChildNodes();

                for (int j = 0; j < typeNodes.getLength(); j++) {
                    Node typeItem = typeNodes.item(j);
                    log("Processing type node: " + typeItem.getLocalName());

                    if ("typeName".equals(typeItem.getLocalName())) {
                        roomType.setTypeName(typeItem.getTextContent());
                        log("Room Type Name: " + roomType.getTypeName());
                    } else if ("maxGuests".equals(typeItem.getLocalName())) {
                        roomType.setMaxGuests(Integer.parseInt(typeItem.getTextContent()));
                        log("Room Max Guests: " + roomType.getMaxGuests());
                    }
                }
                room.setType(roomType);
            } else if ("number".equals(item.getLocalName())) {
                room.setNumber(Integer.parseInt(item.getTextContent()));
                log("Room Number: " + room.getNumber());
            } else if ("price".equals(item.getLocalName())) {
                room.setPrice(new BigDecimal(item.getTextContent()));
                log("Room Price: " + room.getPrice());
            }
        }
        return room;
    }

    public void saveBookings(Bookings bookings, String outputPath, String xsdFilePath) throws ParserConfigurationException, TransformerException, SAXException {
        // Настройка схемы для валидации
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsdFilePath));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        dbFactory.setSchema(schema);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        // Создаем корневой элемент <bookings>
        Element rootElement = doc.createElementNS("http://example.com/hotel-booking/bookings", "bookings");
//        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:tns", "http://example.com/hotel-booking/bookings");
        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:hotel", "http://example.com/hotel-booking/hotel");
        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:room", "http://example.com/hotel-booking/room");
        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:roomType", "http://example.com/hotel-booking/room-type");
        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "http://example.com/hotel-booking/bookings bookings.xsd");
        doc.appendChild(rootElement);

        // Добавление каждого объекта Booking в XML
        for (Booking booking : bookings.getBooking()) {
            Element bookingElement = doc.createElementNS("http://example.com/hotel-booking/bookings", "booking");
            bookingElement.setAttribute("id", String.valueOf(booking.getId()));

            if (booking.getHotel() != null) {
                appendHotel(doc, bookingElement, booking.getHotel());
            }
            if (booking.getRoom() != null) {
                appendRoom(doc, bookingElement, booking.getRoom());
            }
            appendTextElement(doc, bookingElement, "clientName", booking.getClientName());
            appendTextElement(doc, bookingElement, "startDate", booking.getStartDate().toXMLFormat());
            appendTextElement(doc, bookingElement, "endDate", booking.getEndDate().toXMLFormat());
            appendTextElement(doc, bookingElement, "status", booking.getStatus());
            if (booking.getGuestCount() != null) {
                appendTextElement(doc, bookingElement, "guestCount", String.valueOf(booking.getGuestCount()));
            }

            rootElement.appendChild(bookingElement);
        }

        // Трансформация и запись в XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outputPath));
        transformer.transform(source, result);

        System.out.println("Bookings have been saved to XML file.");
    }

    // Вспомогательный метод для добавления Hotel
    private void appendHotel(Document doc, Element bookingElement, Hotel hotel) {
        Element hotelElement = doc.createElementNS("http://example.com/hotel-booking/hotel", "hotel:hotel");
        appendTextElement(doc, hotelElement, "hotel:name", hotel.getName());

        Element addressElement = doc.createElementNS("http://example.com/hotel-booking/hotel", "hotel:address");
        appendTextElement(doc, addressElement, "hotel:street", hotel.getAddress().getStreet());
        appendTextElement(doc, addressElement, "hotel:city", hotel.getAddress().getCity());
        hotelElement.appendChild(addressElement);

        bookingElement.appendChild(hotelElement);
    }

    // Вспомогательный метод для добавления Room
    private void appendRoom(Document doc, Element bookingElement, Room room) {
        Element roomElement = doc.createElementNS("http://example.com/hotel-booking/room", "room:room");

        // Создание элемента типа комнаты
        Element typeElement = doc.createElementNS("http://example.com/hotel-booking/room", "room:type");
        Element typeNameElement = doc.createElementNS("http://example.com/hotel-booking/room-type", "roomType:typeName");
        typeNameElement.setTextContent(room.getType().getTypeName());
        typeElement.appendChild(typeNameElement);

        Element maxGuestsElement = doc.createElementNS("http://example.com/hotel-booking/room-type", "roomType:maxGuests");
        maxGuestsElement.setTextContent(String.valueOf(room.getType().getMaxGuests()));
        typeElement.appendChild(maxGuestsElement);

        roomElement.appendChild(typeElement);
        appendTextElement(doc, roomElement, "room:number", String.valueOf(room.getNumber()));
        appendTextElement(doc, roomElement, "room:price", room.getPrice().toPlainString());

        bookingElement.appendChild(roomElement);
    }

    // Вспомогательный метод для добавления текстового элемента
    private void appendTextElement(Document doc, Element parent, String tagName, String textContent) {
        String namespaceURI = parent.getNamespaceURI(); // Использование пространства имен родителя
        Element element = doc.createElementNS(namespaceURI, tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }




    public static void main(String[] args) {
        try {
            DOMParser parser = new DOMParser();
            String xmlFilePath = "src/main/resources/bookings.xml";
            String xsdFilePath = "src/main/resources/bookings.xsd";

            // Демаршалинг XML в объекты Bookings
            Bookings bookings = parser.unmarshallBookings(xmlFilePath, xsdFilePath);

            // Вывод информации о всех бронированиях
            System.out.println(bookings); // вызовет toString() для Bookings
            parser.saveBookings(bookings,  "src/main/resources/result-bookings.xml", "src/main/resources/bookings.xsd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
