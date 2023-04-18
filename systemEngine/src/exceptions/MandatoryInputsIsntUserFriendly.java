package exceptions;

public class MandatoryInputsIsntUserFriendly extends Exception{

    private final String EXCEPTION_MESSAGE = "There mandatory inputs that isn't user friendly.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
