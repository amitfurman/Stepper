package jaxb.schema;

import dto.DTOAllStepperFlows;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.impl.FlowsManager;
import flow.impl.Stepper2Flows;
import jaxb.schema.generated.STFlow;
import jaxb.schema.generated.STStepInFlow;
import jaxb.schema.generated.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SchemaBasedJAXBMain {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public FlowsManager schemaBasedJAXB(String filePath) throws JAXBException, FileNotFoundException, DuplicateFlowsNames, UnExistsStep, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD, UnExistsOutput, FreeInputsWithSameNameAndDifferentType,InitialInputIsNotExist {
        InputStream inputStream = new FileInputStream(new File(filePath));
        STStepper stepper = deserializeFrom(inputStream);
        verifyIfExistsFlowsWithDuplicateNames(stepper);
        ReferenceToUnExistsStep(stepper);
        Stepper2Flows step = new Stepper2Flows(stepper);

        return new FlowsManager(step.getAllFlows(),step.getNumberOfThreads());
    }



    private static STStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STStepper) u.unmarshal(in);
    }

    public void verifyIfExistsFlowsWithDuplicateNames(STStepper stepper) throws DuplicateFlowsNames {
        List<STFlow> stFlows = stepper.getSTFlows().getSTFlow();

        boolean isDuplicateNames =
                stFlows.stream()
                        .anyMatch(flow ->
                                stFlows.stream()
                                        .filter(f -> !f.equals(flow))
                                        .anyMatch((otherFlow ->
                                                otherFlow.getName().equals(flow.getName()))));
        if (isDuplicateNames) {
            throw new DuplicateFlowsNames();
        }
    }

    public void ReferenceToUnExistsStep(STStepper stepper) throws UnExistsStep {
        List<STFlow> stFlows = stepper.getSTFlows().getSTFlow();
        List<String> stepsNames = new ArrayList<>(Arrays.asList("Spend Some Time", "Collect Files In Folder", "Files Deleter", "Files Renamer", "Files Content Extractor", "CSV Exporter", "Properties Exporter", "File Dumper", "Zipper", "Command Line"));

        for (STFlow flow : stFlows) {
            List<STStepInFlow> stStepFlow = flow.getSTStepsInFlow().getSTStepInFlow();
            for (STStepInFlow step : stStepFlow) {
                String stepName = step.getName();
                boolean isPresent =
                        stepsNames.
                                stream().anyMatch(str -> str.equals(stepName));
                if (!isPresent)
                    throw new UnExistsStep();
            }
        }
    }
}

/*    public void saveToFile(String path, List<FlowDefinition> flowDefinitionList) {
        //help me implement this method using serialization:

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(flowDefinitionList);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

/*    public List<FlowDefinition> loadFromFile(String path) {
        //help me implement this method using serialization:

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            List<FlowDefinition> flowDefinitionList = (List<FlowDefinition>) in.readObject();
            in.close();
            return flowDefinitionList;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}*/
