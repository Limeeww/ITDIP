package org.example.parser;

public interface Const {
    //XML and XSD files
    String XML_FILE = "src/main/resources/bookings.xml";
    String RESULT_XML_FILE = "src/main/resources/result-bookings.xml";
    String INVALID_XML_FILE = "src/main/resources/bookingsNotValid.xml";
    String XSD_FILE = "src/main/resources/bookings.xsd";
    Class<?> BOOKINGS_OBJECT_FACTORY = org.example.entity.bookings.ObjectFactory.class;

    // Namespaces and schema locations
    String BOOKINGS_NAMESPACE_URI = "http://example.com/hotel-booking/bookings";
    String HOTEL_NAMESPACE_URI = "http://example.com/hotel-booking/hotel";
    String ROOM_NAMESPACE_URI = "http://example.com/hotel-booking/room";
    String ROOM_TYPE_NAMESPACE_URI = "http://example.com/hotel-booking/room-type";
    String SCHEMA_LOCATION_URI = "http://example.com/hotel-booking/bookings bookings.xsd";

}