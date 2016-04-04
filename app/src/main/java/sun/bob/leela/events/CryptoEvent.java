package sun.bob.leela.events;

/**
 * Created by bob.sun on 16/1/25.
 */
public class CryptoEvent {
    private String result, field;
    private int type;

    public CryptoEvent(String result, int type){
        this.result = result;
        this.type = type;
    }

    public CryptoEvent(String result, int type, String field){
        this.result = result;
        this.type = type;
        this.field = field;
    }

    public String getResult() {
        return result;
    }

    public int getType() {
        return type;
    }

    public String getField() {
        return field;
    }
}
