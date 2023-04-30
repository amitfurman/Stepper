package xml;

import exceptions.FileIsNotXmlTypeException;
import exceptions.FileNotExistsException;
import java.io.File;

public class XmlValidator {

    public void isXmlFileValid(String filePath) throws FileNotExistsException, FileIsNotXmlTypeException {
        File xmlFile = new File(filePath);

        // Check if file exists
        if (!xmlFile.exists()) {
            throw new FileNotExistsException();
        }
        //Check if file is of XML type
        if (!filePath.endsWith(".xml")) {
            throw new FileIsNotXmlTypeException();
        }
    }

}
