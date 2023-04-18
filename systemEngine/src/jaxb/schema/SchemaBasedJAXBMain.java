package jaxb.schema;

import flow.validator.VerifyFlow;
import jaxb.schema.generated.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaBasedJAXBMain {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public void schemaBasedJAXBMain() {
        try {
            InputStream inputStream = new FileInputStream(new File("systemEngine/src/resources/ex1.xml"));
            STStepper stepper = deserializeFrom(inputStream);

            VerifyFlow flow = new VerifyFlow(stepper);


        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static STStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STStepper) u.unmarshal(in);
    }
}