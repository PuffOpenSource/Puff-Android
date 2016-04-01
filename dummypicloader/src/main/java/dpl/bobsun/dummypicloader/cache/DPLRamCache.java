package dpl.bobsun.dummypicloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.util.IllegalFormatException;

/**
 * Created by bobsun on 15-6-1.
 */
public class DPLRamCache {
    private static DPLRamCache staticInstance;
    LruCache<String, Bitmap> cache ;
    private int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;

    private DPLRamCache(){
        Log.e("maxSize",""+maxSize);
        cache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value){
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static DPLRamCache getStaticInstance(){
        if (staticInstance == null){
            staticInstance = new DPLRamCache();
        }
        return staticInstance;
    }

    public synchronized boolean put(String tag, Bitmap bitmap){
        if (tag == null ||tag.equals("") || bitmap == null){
            return false;
        }
        cache.remove(tag);
        cache.put(tag,bitmap);
        Log.e("RamCache","put");
        return true;
    }

    public synchronized Bitmap get(String tag){
        return cache.get(tag);
    }

    public synchronized void clear(){
        cache.evictAll();
    }


    /**
     * Resize cache.
     * @param size
     * Default size is max runtime memory divide 8.
     */
    public void resize(int size){
        if (size <= 0)
            return;
        maxSize = size;
        cache.resize(size);
    }


}
