package exceptions;

public class TheSameDD extends Exception{

    private final String EXCEPTION_MESSAGE = "Invalid flow! The source data is not the same data definition as the target data.\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
