package exceptions;

public class UnExistsDataInTargetFlow extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! The data does not exists in the target flow\n";
    @Override
    public String getMessage() { return EXCEPTION_MESSAGE;}
}
