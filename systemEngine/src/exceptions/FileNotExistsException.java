package exceptions;

public class FileNotExistsException extends Exception{
    private final String EXCEPTION_MESSAGE = "The file is not exists.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
