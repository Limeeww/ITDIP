package org.example;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XSLTransform {

    public static void main(String[] args) {
        // Шлях до XSL файлу стилізації для bookings
        String xslFilePath = "src/main/resources/bookings.xsl";
        // Шлях до XML файлу з даними про бронювання
        String xmlFilePath = "src/main/resources/bookings.xml";
        // Шлях до HTML файлу для збереження результату
        String htmlOutputPath = "target/bookings.html";

        try {
            // Виконуємо трансформацію
            transformXMLToHTML(xslFilePath, xmlFilePath, htmlOutputPath);
            System.out.println("Трансформація виконана!");
        } catch (TransformerException e) {
            System.err.println("Помилка трансформації: " + e.getMessage());
        }
    }

    private static void transformXMLToHTML(String xslPath, String xmlPath, String htmlPath) throws TransformerException {
        // Створюємо фабрику та трансформер для виконання XSLT трансформації
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(new File(xslPath)));

        // Застосовуємо трансформацію: XML + XSL -> HTML
        transformer.transform(new StreamSource(new File(xmlPath)), new StreamResult(new File(htmlPath)));

        System.out.println("XSL файл : " + xslPath);
        System.out.println("XML файл : " + xmlPath);
        System.out.println("HTML вихідний файл : " + htmlPath);
    }
}
