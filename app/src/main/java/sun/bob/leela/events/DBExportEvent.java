package sun.bob.leela.events;

/**
 * Created by bob.sun on 16/8/9.
 */
public class DBExportEvent {
    public boolean success;
    public String filePath;

    public DBExportEvent(boolean success, String filePath) {
        this.success = success;
        this.filePath = filePath;
    }
}
