package org.example;

import javax.xml.transform.TransformerException;

public class App {
    public static void main(String[] args) throws TransformerException {
        // Викликаємо XSLTransform для виконання трансформації з bookings.xml до bookings.html
        XSLTransform.main(new String[]{
                "src/main/resources/bookings.xsl",  // шлях до XSL файлу
                "src/main/resources/bookings.xml",  // шлях до XML файлу
                "target/bookings.html"              // шлях до вихідного HTML файлу
        });
    }
}
