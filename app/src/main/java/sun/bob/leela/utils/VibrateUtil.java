package sun.bob.leela.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by bob.sun on 16/9/7.
 */
public class VibrateUtil {
    private static VibrateUtil staticInstance;
    private Context context;
    private Vibrator vibrator;
    private VibrateUtil(Context context){
        this.context = context;
        vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    public static VibrateUtil getStaticInstance(Context context){
        if(staticInstance == null)
            staticInstance = new VibrateUtil(context);
        return staticInstance;
    }

    public void tick() {
        vibrator.vibrate(50);
    }
}
