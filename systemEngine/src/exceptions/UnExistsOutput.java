package exceptions;

public class UnExistsOutput extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! There is at least one flow output that is not exists.\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
