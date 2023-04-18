package exceptions;

public class DuplicateFlowsNames extends Exception{

    private final String EXCEPTION_MESSAGE = "There are flows with duplicate names.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
