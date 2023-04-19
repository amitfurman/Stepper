package flow.validator;

import exceptions.DuplicateFlowsNames;
import exceptions.OutputsWithSameName;
import exceptions.UnExistsStep;
import jaxb.schema.generated.STFlow;
import jaxb.schema.generated.STFlows;
import jaxb.schema.generated.STStepInFlow;
import jaxb.schema.generated.STStepper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VerifyFlow {
    private STStepper stepper;
    private List<String> stepsNames;
    private List<String> outputsNames;

    public VerifyFlow(STStepper stepper) {
        this.stepper = stepper;
        stepsNames = new ArrayList<>(Arrays.asList("Spend Some Time", "Collect Files In Folder", "Files Deleter", "Files Renamer", "Files Content Extractor", "CSV Exporter", "Properties Exporter", "File Dumper"));
        outputsNames = new ArrayList<>();
    }

    public void verifyIfExistsFlowsWithDuplicateNames() throws DuplicateFlowsNames {
        List<STFlow> stFlows = stepper.getSTFlows().getSTFlow();

        boolean isDuplicateNames =
                stFlows.stream()
                        .anyMatch(flow ->
                                stFlows.stream()
                                        .filter(f -> !f.equals(flow))
                                        .anyMatch((otherFlow ->
                                                otherFlow.getName().equals(flow.getName()))));
        if(isDuplicateNames) {
            throw new DuplicateFlowsNames();
        }
    }

    public void ReferenceToUnExistsStep() throws UnExistsStep {
        List<STFlow> stFlows = stepper.getSTFlows().getSTFlow();

        for(STFlow flow : stFlows) {
            List<STStepInFlow> stStepFlow = flow.getSTStepsInFlow().getSTStepInFlow();
            for (STStepInFlow step : stStepFlow) {
                String stepName = step.getName();
                boolean isPresent =
                        stepsNames.
                                stream().anyMatch(str -> str.equals(stepName));
                if(!isPresent)
                    throw new UnExistsStep();
            }
        }
    }


}
