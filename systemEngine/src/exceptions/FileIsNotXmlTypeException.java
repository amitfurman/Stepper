package exceptions;

public class FileIsNotXmlTypeException extends Exception{
    private final String EXCEPTION_MESSAGE = "The file is not of XML type.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}

