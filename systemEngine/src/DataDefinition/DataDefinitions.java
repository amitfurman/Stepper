package DataDefinition;

public interface DataDefinitions<T> {
    public boolean isUserFriendly();

    public String name();

    public String toString();

    public String userPresentation();

     public T getValue();


}
