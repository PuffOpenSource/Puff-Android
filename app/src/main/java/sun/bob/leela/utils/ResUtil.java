package sun.bob.leela.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import sun.bob.leela.R;

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

    public Uri getBmpUri(String fileName) {
        if (fileName.startsWith("/")) {
            return Uri.fromFile(new File(fileName));
        } else {
            return Uri.parse("file:///android_asset/" + fileName);
        }

    }

    public Object getBmpPath(String fileName) {
        if (fileName.startsWith("/")) {
            return fileName;
        } else {
            InputStream ret;
            try {
                ret =  context.getResources().getAssets().open(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return ret;
        }
    }

    public AppCompatDialog showProgressbar(Activity activity, long timeout, boolean cancelable) {
        final AppCompatDialog dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(cancelable);
        dialog.setTitle("Progressing...");
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        if (timeout > 0) {
            Handler handler = new Handler(activity.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    dialog.dismiss();
                }
            }, timeout);
            dialog.show();
        } else {
            dialog.show();
        }
        return dialog;
    }

    public AppCompatDialog showProgressbar(Activity activity, long timeout) {
        return showProgressbar(activity, timeout, true);
    }

    public AppCompatDialog showProgressbar(Activity activity) {
        return showProgressbar(activity, 0, false);
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
