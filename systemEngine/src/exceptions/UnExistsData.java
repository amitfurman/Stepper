package exceptions;

public class UnExistsData extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! The data does not exists in the current flow.\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
