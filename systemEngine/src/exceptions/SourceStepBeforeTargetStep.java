package exceptions;

public class SourceStepBeforeTargetStep extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! The target step is before the source step.\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
