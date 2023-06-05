package flow.impl;

import flow.api.FlowDefinition;

import java.util.LinkedList;

public class FlowsManager {

    LinkedList<FlowDefinition> allFlows;
    int numberOfThreads;

    public FlowsManager(LinkedList<FlowDefinition> allFlows, int numberOfThreads) {
        this.allFlows = allFlows;
        this.numberOfThreads = numberOfThreads;
    }

    public LinkedList<FlowDefinition> getAllFlows() {
        return allFlows;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }
}
