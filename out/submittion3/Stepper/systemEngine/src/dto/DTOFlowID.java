package dto;

import flow.execution.FlowExecution;

import java.util.UUID;

public class DTOFlowID {

    private UUID uniqueId;

    public DTOFlowID(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() { return uniqueId.toString(); }
    public UUID getUniqueIdByUUID() { return uniqueId; }
}
