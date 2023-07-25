package dto;
public class DTOStepsInFlow {
    private String originalName;
    private String finalName;
    private String result;

    public DTOStepsInFlow( String originalName,String finalName, String result){
        this.originalName = originalName;
        this.finalName = finalName;
        this.result = result;
    }
    public String getOriginalName() {return originalName;}
    public String getFinalName() {return finalName;}
    public String getResult() {return result;}

}
