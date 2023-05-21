package exceptions;

public class InitialInputIsNotFreeInput extends Exception {

    private final String EXCEPTION_MESSAGE = "Invalid flow! There are initial inputs that are not free input";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
