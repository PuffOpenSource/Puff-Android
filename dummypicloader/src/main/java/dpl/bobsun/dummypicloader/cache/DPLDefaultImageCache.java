package dpl.bobsun.dummypicloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by bobsun on 15-6-9.
 */
public class DPLDefaultImageCache {
    private static DPLDefaultImageCache staticInstance;
    private HashMap<Integer,WeakReference<Bitmap>> cache;
    private Context appContext;
    private DPLDefaultImageCache(Context context){
        appContext = context;
        cache = new HashMap<>();
    }

    public static DPLDefaultImageCache getStaticInstance(Context context){
        if (staticInstance == null){
            staticInstance = new DPLDefaultImageCache(context);
        }
        return staticInstance;
    }

    public void add(int id){
        if (exists(id))
            return;
        cache.put(id,new WeakReference<Bitmap>(BitmapFactory.decodeResource(appContext.getResources(),id)));
    }

    public boolean exists(int id){
        return cache.containsKey(id);
    }

    public Bitmap get(int id){
        WeakReference<Bitmap> ret = cache.get(id);
        if(ret.get() == null){
            return BitmapFactory.decodeResource(appContext.getResources(),id);
        }
        return ret.get();
    }
}
