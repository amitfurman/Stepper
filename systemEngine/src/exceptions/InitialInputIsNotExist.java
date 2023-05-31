package exceptions;

public class InitialInputIsNotExist extends Exception {

    private final String EXCEPTION_MESSAGE = "Invalid flow! There are initial inputs that are not exist in the current flow";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
