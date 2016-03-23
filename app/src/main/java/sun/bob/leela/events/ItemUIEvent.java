package sun.bob.leela.events;

/**
 * Created by bob.sun on 16/3/23.
 */
public class ItemUIEvent {
    public static final int LIST_SCROLLED = 0xE1;
    public static final int LIST_STOPPED  = 0xE2;

    private int type;
    private int dy;

    public ItemUIEvent(int type, int dy) {
        this.type = type;
        this.dy = dy;
    }

    public int getType() {
        return type;
    }

    public int getDy() {
        return dy;
    }
}
