package dto;

import java.util.List;

public class DTOFlowDefinitionInRoles {
    String flowName;
    String description;
    Integer numberOfSteps;
    Integer numberOfFreeInputs;
    Integer numberOfContinuations;
    Boolean isReadOnly;
    List<String> flowFormalOutputs;
    List<DTOStepUsageDeclaration> stepsInFlow;
    List<DTOFlowOutputs> allOutputs;
    List<DTOFreeInputs> freeInputs;


    public DTOFlowDefinitionInRoles(String flowName, String description, Integer numberOfSteps, Integer numberOfFreeInputs, Integer numberOfContinuations, Boolean isReadOnly,
                                    List<String> flowFormalOutputs, List<DTOStepUsageDeclaration> stepsInFlow, List<DTOFlowOutputs> flowOutputs, List<DTOFreeInputs> freeInputs) {
        this.flowName = flowName;
        this.description = description;
        this.numberOfSteps = numberOfSteps;
        this.numberOfFreeInputs = numberOfFreeInputs;
        this.numberOfContinuations = numberOfContinuations;
        this.isReadOnly = isReadOnly;
        this.flowFormalOutputs = flowFormalOutputs;
        this.stepsInFlow = stepsInFlow;
        this.allOutputs = flowOutputs;
        this.freeInputs = freeInputs;

    }

    public String getFlowName() {return flowName;}
    public String getDescription() {return description;}
    public Integer getNumberOfSteps() {return numberOfSteps;}
    public Integer getNumberOfFreeInputs() {return numberOfFreeInputs;}
    public Integer getNumberOfContinuations() {return numberOfContinuations;}
    public Boolean getIsReadOnly() {return isReadOnly;}
    public List<String> getFlowFormalOutputs() {return flowFormalOutputs;}
    public List<DTOStepUsageDeclaration> getStepsInFlow() {return stepsInFlow;}
    public List<DTOFlowOutputs> getAllOutputs() {return allOutputs;}
    public List<DTOFreeInputs> getFreeInputs() {return freeInputs;}


}
