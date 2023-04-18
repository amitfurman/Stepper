package readingsysteminformationfile;

import exceptions.FileIsNotXmlTypeException;
import exceptions.FileNotExistsException;
import jaxb.schema.SchemaBasedJAXBMain;
import xml.XmlValidator;

import java.util.Scanner;

public class ReadingInformationMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the path to the XML file:");
        String filePath = scanner.nextLine();
        XmlValidator validator = new XmlValidator();

        try {
            validator.isXmlFileValid(filePath);
            System.out.println("XML file is valid");
        } catch (FileNotExistsException e) {
            System.out.println(e.getMessage() + "Please provide a valid XML file.");
        } catch (FileIsNotXmlTypeException e) {
            System.out.println(e.getMessage() + "Please provide a valid XML file.");
        }
        ////asking for new xml

        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        schema.schemaBasedJAXBMain();


    }
}