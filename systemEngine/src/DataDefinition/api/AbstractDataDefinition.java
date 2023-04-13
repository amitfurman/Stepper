package DataDefinition.api;

public abstract class AbstractDataDefinition implements DataDefinitions {

    private final String name;
    private final boolean userFriendly;
    private final Class<?> type;

    protected AbstractDataDefinition(String name, boolean userFriendly, Class<?> type) {
        this.name = name;
        this.userFriendly = userFriendly;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isUserFriendly() {
        return userFriendly;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
