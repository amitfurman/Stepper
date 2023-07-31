package exceptions;

public class OutputsWithSameName extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! There are several outputs with the same name\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}