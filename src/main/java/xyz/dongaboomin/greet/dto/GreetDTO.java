package xyz.dongaboomin.greet.dto;

public class GreetDTO {
    private  String greetText;

    public GreetDTO(String greetText){
        this.greetText = greetText;
    }

    public String getGreetText() {
        return greetText;
    }

    public void setGreetText(String greetText) {
        this.greetText = greetText;
    }
}
