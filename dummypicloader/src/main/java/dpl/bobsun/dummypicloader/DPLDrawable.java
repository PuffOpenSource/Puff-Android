package dpl.bobsun.dummypicloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by bobsun on 15-5-26.
 */
public class DPLDrawable extends BitmapDrawable {
    private WeakReference DPLTaskRef;
    private boolean hasDefault;
    public DPLDrawable(Resources res, String fileName,DPLTask task){
        DPLTaskRef = new WeakReference(task);
    }
    public DPLDrawable(Resources res, Bitmap bitmap,DPLTask task){
        super(res,bitmap);
        hasDefault = true;
        DPLTaskRef = new WeakReference(task);
    }
    public DPLTask getTask(){
        return (DPLTask) DPLTaskRef.get();
    }

    public boolean isHasDefault(){
        return hasDefault;
    }
}
