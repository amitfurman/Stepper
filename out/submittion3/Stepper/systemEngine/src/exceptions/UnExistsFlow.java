package exceptions;

public class UnExistsFlow extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! The target flow in the continuation does not defined in the current xml";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
