package hva.sne.bas.numbertrivia;

/**
 * Created by basb on 4-2-2018.
 */

public class Trivia {
    String number;
    String text;

    public Trivia(String number, String text) {
        this.number = number;
        this.text = text;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
