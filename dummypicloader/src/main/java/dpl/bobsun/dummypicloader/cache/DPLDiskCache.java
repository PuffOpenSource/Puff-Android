package dpl.bobsun.dummypicloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bobsun on 15-6-1.
 */
public class DPLDiskCache {
    private static DPLDiskCache staticInstance;
    private String cacheFolderPath = Environment.getExternalStorageDirectory().getPath() + "/data/DPLCache/";
    private File cacheFolder;
    private DPLDiskCache(){
        cacheFolder = new File(cacheFolderPath);
        if (!cacheFolder.exists()){
            cacheFolder.mkdirs();
        }
    }

    /**
     * Get a static instance.
     * This class is singlton.
     * @return
     */
    public static DPLDiskCache getStaticInstance(){
        if (staticInstance == null)
            staticInstance = new DPLDiskCache();
        return staticInstance;
    }

    /**
     * Determine specified image is cached or not.
     * @param key
     * Key value, usually be a URL address.
     * @return
     * true if this image is cached.
     * false if this image can not be found.
     */
    public boolean isCached(String key){
        return new File(cacheFolderPath + key.hashCode()).exists();
    }

    /**
     * Put a new bitmap into disk cache.
     * If the bitmap already cached in disk, it <b>WILL BE OVERWRITTEN</b>
     * @param key
     * Key value.
     * @param value
     * Bitmap to be cached.
     */
    public synchronized void put(String key, Bitmap value){
        //Todo
        //Question, will this cause a performance issue?
        //        UUID.fromString(key);
        //Maybe it will when calling "toString()"
        if (value == null)
            return;
        File cacheFile = new File(cacheFolderPath + key.hashCode());
        if (!cacheFile.delete()){
            try {
                cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            value.compress(Bitmap.CompressFormat.PNG,80,new FileOutputStream(cacheFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * Get file path for specific key.
     * @param key
     *  Key value, usually be an URL.
     * @return
     *  <b>Cached Image File Path</b>
     */
    public synchronized String get(String key){
        return cacheFolderPath + key.hashCode();
    }

    /**
     * Change cache storage folder.
     * <b>Default folder is getExternalStorage()+/data/DPLCache/</b>.
     * All cached image will lays there.
     * @param newLocation
     * New Location
     */
    public void changeCacheFolder(String newLocation){
        cacheFolderPath = newLocation;
        cacheFolder = new File(cacheFolderPath);
    }

    public String getCacheFolderPath(){
        return cacheFolderPath;
    }

    class ImageCacheFinder implements FilenameFilter{

        String dest;
        public ImageCacheFinder(String dest){
            this.dest = dest;
        }
        @Override
        public boolean accept(File dir, String filename) {
            if (filename.endsWith(dest))
                return true;
            return false;
        }
    }

}
