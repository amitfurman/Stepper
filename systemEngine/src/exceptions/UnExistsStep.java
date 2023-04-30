package exceptions;

public class UnExistsStep extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! There is a reference to UnExists step.\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
