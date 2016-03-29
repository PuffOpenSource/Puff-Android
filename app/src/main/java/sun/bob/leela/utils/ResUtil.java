package sun.bob.leela.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;

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

    public Bitmap getBmpInAssets(String assetName) throws IOException {
        return BitmapFactory.decodeStream(context.getResources().getAssets().open(assetName));
    }

    public Bitmap getBmp(String fileName) throws IOException {
        if (fileName.startsWith("/")) {
            //File System
            return BitmapFactory.decodeStream(new FileInputStream(fileName));
        } else {
            //Assets
            return BitmapFactory.decodeStream(context.getResources().getAssets().open(fileName));
        }
    }
}
