package exceptions;

public class DuplicateFlowsNames extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! There are flows with duplicate names\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
