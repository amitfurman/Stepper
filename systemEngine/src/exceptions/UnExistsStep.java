package exceptions;

public class UnExistsStep extends Exception{

    private final String EXCEPTION_MESSAGE = "There is reference to UnExists step.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
