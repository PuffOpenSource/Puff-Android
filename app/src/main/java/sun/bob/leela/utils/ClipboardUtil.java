package sun.bob.leela.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by bob.sun on 16/4/25.
 */
public class ClipboardUtil {

    private static ClipboardUtil ourInstance;
    private Context context;
    private ClipboardManager manager;

    public static ClipboardUtil getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new ClipboardUtil(context);
        return ourInstance;
    }

    private ClipboardUtil(Context context) {
        this.context = context;
        manager = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void setText(String text) {
        ClipData clipData = ClipData.newPlainText("", text);
        manager.setPrimaryClip(clipData);
    }

    public String getText()  {
        return "";
    }
}
