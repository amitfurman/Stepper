package dto;

public class DTOFreeInputsByUserString {
    StringBuilder freeInputsByUserString;
    int numberOfFreeInputs;

    public DTOFreeInputsByUserString(StringBuilder freeInputsByUserString, int numberOfFreeInputs) {
        this.freeInputsByUserString = freeInputsByUserString;
        this.numberOfFreeInputs = numberOfFreeInputs;
    }
    public StringBuilder getFreeInputsByUserString() {
        return this.freeInputsByUserString;
    }
    public int getNumberOfFreeInputs() { return this.numberOfFreeInputs;}
}
