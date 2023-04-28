package exceptions;

public class MandatoryInputsIsntUserFriendly extends Exception{

    private final String EXCEPTION_MESSAGE = "Invalid flow! There are mandatory inputs that aren't user friendly.\n";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
