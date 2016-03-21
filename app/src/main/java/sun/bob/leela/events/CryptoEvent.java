package sun.bob.leela.events;

/**
 * Created by bob.sun on 16/1/25.
 */
public class CryptoEvent {
    private String result;
    private int type;

    public CryptoEvent(String result, int type){
        this.result = result;
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public int getType() {
        return type;
    }
}
