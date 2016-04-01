package dpl.bobsun.dummypicloader.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by bob.sun on 15/8/5.
 */
public class DisplayUtil {
    private static DisplayUtil staticInstance;
    private Context mContext;
    private int screenWidth, screenHeight, scaledHeight;
    private DisplayUtil(Context context){
        mContext = context;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        scaledHeight = (int) (((float) screenWidth / (float) screenHeight) * screenWidth);
    }

    public static DisplayUtil getStaticInstance(Context context){
        if (staticInstance == null)
            staticInstance = new DisplayUtil(context);
        return staticInstance;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScaledHeight() {
        return scaledHeight;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
}
