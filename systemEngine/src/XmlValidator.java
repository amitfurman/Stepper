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

public class XmlValidator {
    private String filePath;

    public XmlValidator(String filePath) {
        this.filePath = filePath;
    }

    public void isXmlFileValid() throws FileNotExistsException, FileIsNotXmlTypeException {
        File xmlFile = new File(filePath);
        try {

            // Check if file exists
            if (!xmlFile.exists()) {
                throw new FileNotExistsException();
            }
            //Check if file is of XML type
            if (!filePath.endsWith(".xml")) {
                throw new FileIsNotXmlTypeException();
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filePath));
            doc.getDocumentElement().normalize();


        }


        //לבדוק עם אביעד אם פשוט יעבור לUI ושם יודפס הSTRING
        catch (FileNotExistsException e) {
            System.out.println(e.getMessage());
        } catch (FileIsNotXmlTypeException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }


    }

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
