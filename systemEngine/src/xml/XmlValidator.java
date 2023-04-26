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
