package sun.bob.leela.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by bob.sun on 16/4/1.
 */
public class EnvUtil {
    private static EnvUtil ourInstance;
    private Context context;
    private String sdcardFolder, imageCropCacheFolder, catIconFolder, acctIconFolder;

    public static EnvUtil getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new EnvUtil(context);
        return ourInstance;
    }

    private EnvUtil(Context context) {
        this.context = context;
        sdcardFolder = Environment.getExternalStorageDirectory().getPath();
        imageCropCacheFolder = sdcardFolder + "/data/data/leela/cache/";
        if (!new File(imageCropCacheFolder).exists()) {
            new File(imageCropCacheFolder).mkdirs();
            try {
                new File(imageCropCacheFolder + ".nomedia").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        catIconFolder = sdcardFolder + "/data/data/leela/catIcon/";
        if (!new File(catIconFolder).exists()) {
            new File(catIconFolder).mkdirs();
            try {
                new File(catIconFolder + ".nomedia").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        acctIconFolder = sdcardFolder + "/data/data/leela/acctIcon/";
        if (!new File(acctIconFolder).exists()) {
            new File(acctIconFolder).mkdirs();
            try {
                new File(acctIconFolder + ".nomedia").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageCropCacheFolder() {
        return imageCropCacheFolder;
    }

    public String getSdcardFolder() {
        return sdcardFolder;
    }

    public String getCatIconFolder() {
        return catIconFolder;
    }

    public String getAcctIconFolder() {
        return acctIconFolder;
    }
}
