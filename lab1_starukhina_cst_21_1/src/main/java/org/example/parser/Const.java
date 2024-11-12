package org.example.parser;

public interface Const {

    // Tags for XML elements
    String TAG_BOOKINGS = "bookings";
    String TAG_BOOKING = "booking";
    String TAG_CLIENT_NAME = "clientName";
    String TAG_START_DATE = "startDate";
    String TAG_END_DATE = "endDate";
    String TAG_STATUS = "status";
    String TAG_GUEST_COUNT = "guestCount";
    String TAG_HOTEL = "hotel";
    String TAG_ROOM = "room";

    // Attributes
    String ATTR_ID = "id";

    // XML and XSD files
    String XML_FILE = "src/main/resources/bookings.xml";
    String INVALID_XML_FILE = "src/main/resources/invalid_bookings.xml";
    String XSD_FILE = "src/main/resources/bookings.xsd";
    Class<?> OBJECT_FACTORY = org.example.entity.bookings.ObjectFactory.class;

    // Namespaces and schema locations
    String BOOKINGS_NAMESPACE_URI = "http://example.com/hotel-booking/bookings";
    String HOTEL_NAMESPACE_URI = "http://example.com/hotel-booking/hotel";
    String ROOM_NAMESPACE_URI = "http://example.com/hotel-booking/room";
    String SCHEMA_LOCATION_ATTR_NAME = "schemaLocation";
    String SCHEMA_LOCATION_ATTR_FQN = "xsi:schemaLocation";
    String XSI_NAMESPACE_PREFIX = "xsi";
    String SCHEMA_LOCATION_URI = "http://example.com/hotel-booking/bookings bookings.xsd";

    // Validation features
    String FEATURE_TURN_VALIDATION_ON = "http://xml.org/sax/features/validation";
    String FEATURE_TURN_SCHEMA_VALIDATION_ON = "http://apache.org/xml/features/validation/schema";
}