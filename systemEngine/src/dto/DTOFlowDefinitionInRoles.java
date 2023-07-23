package dto;

public class DTOFlowDefinitionInRoles {
    String flowName;
    String description;
    Integer numberOfSteps;
    Integer numberOfFreeInputs;
    Integer numberOfContinuations;

    public DTOFlowDefinitionInRoles(String flowName, String description, Integer numberOfSteps, Integer numberOfFreeInputs, Integer numberOfContinuations) {
        this.flowName = flowName;
        this.description = description;
        this.numberOfSteps = numberOfSteps;
        this.numberOfFreeInputs = numberOfFreeInputs;
        this.numberOfContinuations = numberOfContinuations;
    }

    public String getFlowName() {return flowName;}
    public String getDescription() {return description;}
    public Integer getNumberOfSteps() {return numberOfSteps;}
    public Integer getNumberOfFreeInputs() {return numberOfFreeInputs;}
    public Integer getNumberOfContinuations() {return numberOfContinuations;}


}
