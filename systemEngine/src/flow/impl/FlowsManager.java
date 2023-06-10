package flow.impl;

import flow.api.FlowDefinition;
import flow.mapping.FlowContinuationMapping;

import java.util.LinkedList;

public class FlowsManager {

    LinkedList<FlowDefinition> allFlows;

    private LinkedList<FlowContinuationMapping> allContinuationMappings;


    int numberOfThreads;

    public FlowsManager(LinkedList<FlowDefinition> allFlows, int numberOfThreads, LinkedList<FlowContinuationMapping> allContinuationMappings ) {
        this.allFlows = allFlows;
        this.numberOfThreads = numberOfThreads;
        this.allContinuationMappings = allContinuationMappings;
    }

    public LinkedList<FlowDefinition> getAllFlows() {
        return allFlows;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public LinkedList<FlowContinuationMapping> getAllContinuationMappings() {
        return allContinuationMappings;
    }
}
