package sun.bob.leela.utils;

import android.content.Context;

/**
 * Created by bob.sun on 16/3/23.
 */
public class DeviceUtil {
    private static DeviceUtil ourInstance;
    private Context context;
    public static DeviceUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DeviceUtil(context);
        }
        return ourInstance;
    }

    private DeviceUtil(Context context) {
        this.context = context;
    }

    public float getDP(){
        return context.getResources().getDisplayMetrics().density;
    }
}
