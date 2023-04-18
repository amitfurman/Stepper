package xml;

import exceptions.FileIsNotXmlTypeException;
import exceptions.FileNotExistsException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

////UI ASKING FOR XML PATH
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
            /*
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filePath));*/
    //     doc.getDocumentElement().normalize();
   /* public boolean isThereFlowsWithDuplicateName(){
        HashSet<String> flowNames = new HashSet<>();
        NodeList flowNodes = doc.getElementsByTagName("Flow");
        for (int i = 0; i < flowNodes.getLength(); i++) {
            Element flowNode = (Element) flowNodes.item(i);
            String flowName = flowNode.getAttribute("Name");
            if (flowNames.contains(flowName)) {
                System.out.println("Error: Duplicate flow name: " + flowName);
                return;
            }
            flowNames.add(flowName);
    }*/
}
