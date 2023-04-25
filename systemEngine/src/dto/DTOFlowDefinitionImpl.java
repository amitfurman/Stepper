package dto;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import flow.api.StepUsageDeclaration;
import java.util.*;

public class DTOFlowDefinitionImpl implements DTOFlowDefinition{
    private final String name;
    private final String description;
    private boolean isFlowReadOnly;
    private final List<String> flowOutputs;
    private final List<DTOStepUsageDeclaration> steps;
    private final List<SingleFlowIOData> IOlist;
    private final List<DTOSingleFlowIOData> freeInputs;

    public DTOFlowDefinitionImpl(FlowDefinition flow) {
        this.name = flow.getName();
        this.description = flow.getDescription();
        this.isFlowReadOnly = flow.getFlowReadOnly();
        this.flowOutputs = new ArrayList<>(flow.getFlowFormalOutputs());
        this.steps = new LinkedList<>();
        for (StepUsageDeclaration step: flow.getFlowSteps()) {
            this.steps.add(new DTOStepUsageDeclarationImpl(step));
        }
        this.IOlist = new ArrayList<SingleFlowIOData>(flow.getIOlist());
        this.freeInputs = new LinkedList<>();
        for (SingleFlowIOData input: flow.getMandatoryInputsList()) {
          this.freeInputs.add(new DTOSingleFlowIODataImpl(input));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }

    @Override
    public boolean getFlowReadOnly() {
        return this.isFlowReadOnly;
    }

    @Override
    public List<DTOStepUsageDeclaration> getFlowStepsData() {
        return steps;
    }

    @Override
    public List<SingleFlowIOData> getIOlist() {
        return IOlist;
    }

    @Override
    public List<String> getListOfStepsWithCurrInput(String inputName){

        List<String> stepsWithCurrInput = new LinkedList<>();

        for(DTOSingleFlowIOData input : freeInputs){
            if(input.getFinalName().equals(inputName)){
                stepsWithCurrInput.add(input.getStepName());
            }
        }
        return stepsWithCurrInput;
    }

    @Override
    public List<DTOSingleFlowIOData> getFlowFreeInputs() {
        return this.freeInputs;
    }
}
