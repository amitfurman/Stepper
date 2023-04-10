package Steps;

import DataDefinition.NumberDataDefinition;

public class SpendSomeTime implements StepInterface {
    public NumberDataDefinition TIME_TO_SPEND;

     public SpendSomeTime (NumberDataDefinition TIME) {
        if (TIME.isNegative() || TIME.isEqualToZero()) {
            System.out.println("Failed. Time to spend must be a positive number.");
        } else {
            TIME_TO_SPEND = TIME;
        }
     }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    public void sleep() throws InterruptedException {
        // Log before sleeping
        System.out.println("About to sleep for " + TIME_TO_SPEND.getValue() + " seconds...");

        try{
        Thread.sleep(TIME_TO_SPEND.getValue() * 1000); }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Log after sleeping
        System.out.println("Done sleeping...");
    }
}


