package exceptions;

public class OutputsWithSameName extends Exception{

    private final String EXCEPTION_MESSAGE = "There is outputs with same name.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}