package exceptions;

public class FileIsNotXmlTypeException extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! The file is not of XML type.\n";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}

