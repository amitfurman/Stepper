package datadefinition.api;

import flow.api.FlowDefinition;

import java.io.Serializable;

public interface DataDefinitions  {

    String getName();
    boolean isUserFriendly();
    Class<?> getType();
}
