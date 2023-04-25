package dto;

import datadefinition.api.DataDefinitions;
import steps.api.DataNecessity;

public interface DTOSingleFlowIOData {
   DataDefinitions getType();
   String getFinalName();
   String getStepName();
   DataNecessity getNecessity();
}
