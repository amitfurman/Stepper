package exceptions;

public class FileNotExistsException extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid file! The file is not exists\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
