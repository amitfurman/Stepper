package exceptions;

public class InvalidNumberInputException extends Exception{
   /* public InvalidNumberInputException() {
        super("Error: You must enter a number, please try again.");
    }

    */

    private final String EXCEPTION_MESSAGE = "Error: You must enter a number, please try again.\n";
    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
