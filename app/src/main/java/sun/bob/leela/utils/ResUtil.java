package sun.bob.leela.utils;

import android.content.Context;

/**
 * Created by bob.sun on 16/3/23.
 */
public class ResUtil {
    private static ResUtil ourInstance;
    private Context context;
    public static ResUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ResUtil(context);
        }
        return ourInstance;
    }

    private ResUtil(Context context) {
        this.context = context;
    }

    public float getDP(){
        return context.getResources().getDisplayMetrics().density;
    }

    public int pointToDp(int point) {
        return (int) (point * getDP());
    }
}
