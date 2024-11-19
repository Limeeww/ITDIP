package org.example.parser;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.example.entity.bookings.Booking;
import org.example.entity.bookings.Bookings;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;


public class JAXBParser {
    public Bookings unmarshallBookings(String xmlFilePath, String xsdFilePath, Class<?> objectFactory) throws JAXBException, SAXException {
        JAXBContext jaxbContext = JAXBContext.newInstance(objectFactory);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        if (xsdFilePath != null) {
            Schema schema = schemaFactory.newSchema(new File(xsdFilePath));
            unmarshaller.setSchema(schema);
        }

        unmarshaller.setEventHandler(event -> {
            System.err.println("====================================");
            System.err.println(xmlFilePath + " is NOT valid against " + xsdFilePath + ":\n" + event.getMessage());
            System.err.println("====================================");
            return false;
        });

        return (Bookings) unmarshaller.unmarshal(new File(xmlFilePath));
    }


    public static void main(String[] args) throws JAXBException, SAXException {
        JAXBParser jaxbParser = new JAXBParser();
        System.out.println("--== JAXB Parser ==--");
        Bookings bookings = jaxbParser.unmarshallBookings(Const.XML_FILE, Const.XSD_FILE, Const.BOOKINGS_OBJECT_FACTORY);
        System.out.println("====================================");
        System.out.println("Here is the orders: \n" + bookings);
        System.out.println("====================================");
        /*// jaxbParser.unmarshallOrders(Constants.ORDERS_NON_VALID_XML_FILE, Constants.ORDERS_XSD_FILE, Constants.ORDERS_OBJECT_FACTORY_CLASS);
        jaxbParser.saveOrdersToXml(orders, Constants.RESULT_ORDERS_XML_FILE, Constants.ORDERS_XSD_FILE);*/

        boolean isValid = true;
        if (bookings == null || bookings.getBooking().isEmpty()) {
            System.err.println("Bookings data is empty or null.");
        } else {
            isValid = true;
            for (Booking booking : bookings.getBooking()) {
                if (booking.getClientName() == null || booking.getClientName().isEmpty()) {
                    System.err.println("Client name is missing in booking id " + booking.getId());
                    isValid = false;
                }
            }
        }
        if (isValid) {
            try {
                saveBookings(bookings, Const.RESULT_XML_FILE, Const.XSD_FILE, Const.BOOKINGS_OBJECT_FACTORY);
                System.out.println("Bookings have been successfully saved to XML.");
            } catch (Exception ex) {
                System.err.println("====================================");
                System.err.println("Object tree not valid against XSD.");
                System.err.println(ex.getClass().getName());
                System.err.println("====================================");
            }
        }else {
            System.err.println("Data validation failed. Bookings object was not saved.");
        }

        // save Orders object to XML (success)
        // saveOrders(orders, Const.XML_FILE + ".jaxb.xml", Const.XSD_FILE, Const.OBJECT_FACTORY);

    }

    public static void saveBookings(Bookings bookings, String xmlFileName, String xsdFileName, Class<?> objectFactory) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(objectFactory);
        Marshaller marshaller = jc.createMarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        if (xsdFileName != null) {
            Schema schema = sf.newSchema(new File(xsdFileName));

            marshaller.setSchema(schema);
            marshaller.setEventHandler(event -> {
                System.err.println("====================================");
                System.err.println(xmlFileName + " is NOT valid against " + xsdFileName + ":\n" + event.getMessage());
                System.err.println("Error occurred at line " + event.getLocator().getLineNumber() + ", column " + event.getLocator().getColumnNumber());
                System.err.println("====================================");
                return false;
            });
        }

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, Const.SCHEMA_LOCATION_URI);

        marshaller.marshal(bookings, new File(xmlFileName));
    }
}