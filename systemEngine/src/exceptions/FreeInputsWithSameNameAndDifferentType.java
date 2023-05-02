package exceptions;

public class FreeInputsWithSameNameAndDifferentType extends Exception{
    private final String EXCEPTION_MESSAGE = "Invalid flow! There are mandatory inputs with the same name but different type.";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
