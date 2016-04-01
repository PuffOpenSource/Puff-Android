package dpl.bobsun.dummypicloader;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import dpl.bobsun.dummypicloader.cache.DPLDefaultImageCache;
import dpl.bobsun.dummypicloader.cache.DPLDiskCache;
import dpl.bobsun.dummypicloader.cache.DPLRamCache;

/**
 * Created by bobsun on 15-5-26.
 */
public class DPLTask extends AsyncTask<String, Integer, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    BitmapFactory.Options options;
    int type;

    public static final int TASK_TYPE_FILE = 1;
    public static final int TASK_TYPE_URL = 2;
    public static final int TASK_TYPE_RES = 3;
    public static final int TASK_TYPE_URI = 4;

    String cacheKey;
    private Context context;
    private boolean resized;
    private boolean cropped = false;
    private int defaultImgResId = 0;
    private boolean somethingWrong;

    public DPLTask(ImageView imageView,int type){
        imageViewWeakReference = new WeakReference(imageView);
        this.type = type;
    }

    @Override
    protected void onPreExecute(){

    }

    public DPLTask setContext(Context context){
        this.context = context;
        return this;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        cacheKey = strings[0];

        if (resized){
            cacheKey = cacheKey + options.outWidth + options.outHeight;
        }


        InputStream inputStream = null;

        if (this.type == TASK_TYPE_FILE) {

            if (resized){
                BitmapFactory.Options fakeOption = new BitmapFactory.Options();
                fakeOption.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(strings[0],fakeOption);
                options.inScaled = true;
                if ( fakeOption.outWidth / options.outWidth > fakeOption.outHeight / options.outHeight){
                    options.inSampleSize =fakeOption.outWidth / options.outWidth;
                } else {
                    options.inSampleSize = fakeOption.outHeight / options.outHeight;
                }
            }
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            try {
                inputStream = new FileInputStream(strings[0]);
                ((FileInputStream)inputStream).getFD();
            } catch (FileNotFoundException e) {
                somethingWrong = true;
                if (defaultImgResId == 0) {
                    return Bitmap.createBitmap(options.outWidth == 0 ? 300: options.outWidth,
                                                options.outHeight == 0 ? 300: options.outHeight, Bitmap.Config.RGB_565);
                }else{
                    return null;
                }
            } catch (IOException e) {
                somethingWrong = true;
                if (defaultImgResId == 0) {
                    return Bitmap.createBitmap(options.outWidth == 0 ? 300: options.outWidth,
                                                options.outHeight == 0 ? 300: options.outHeight, Bitmap.Config.RGB_565);
                }else{
                    return null;
                }
            }
        }
        if (this.type == TASK_TYPE_URL){
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                urlConnection.getHeaderFields();
                inputStream = urlConnection.getInputStream();

                options.inPreferredConfig = Bitmap.Config.RGB_565;

                if (resized) {
                    BitmapFactory.Options fakeOption = new BitmapFactory.Options();
//                    fakeOption.inJustDecodeBounds = true;
                    DPLDiskCache.getStaticInstance().put(cacheKey,
                            BitmapFactory.decodeStream(urlConnection.getInputStream(), new Rect(), fakeOption)
                    );
                    options.inScaled = true;
                    if (fakeOption.outWidth / options.outWidth > fakeOption.outHeight / options.outHeight) {
                        options.inSampleSize = fakeOption.outWidth / options.outWidth;
                    } else {
                        options.inSampleSize = fakeOption.outHeight / options.outHeight;
                    }
                    inputStream = new FileInputStream(DPLDiskCache.getStaticInstance().get(cacheKey));
                }

            } catch (MalformedURLException e) {
                somethingWrong = true;
                if (defaultImgResId == 0) {
                    return Bitmap.createBitmap(options.outWidth == 0 ? 300: options.outWidth,
                                                options.outHeight == 0 ? 300: options.outHeight, Bitmap.Config.RGB_565);
                }else{
                    return null;
                }
            } catch (IOException e) {
                somethingWrong = true;
                if (defaultImgResId == 0) {
                    return Bitmap.createBitmap(options.outWidth == 0 ? 300: options.outWidth,
                                                options.outHeight == 0 ? 300: options.outHeight, Bitmap.Config.RGB_565);
                }else{
                    return null;
                }
            }
        }
        if (this.type == TASK_TYPE_URI){
            Uri bmpUri = Uri.parse(strings[0]);
            ContentResolver contentResolver = context.getContentResolver();
            try{
                inputStream = contentResolver.openInputStream(bmpUri);
                if (resized) {
                    BitmapFactory.Options fakeOption = new BitmapFactory.Options();
                    fakeOption.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream,new Rect(),fakeOption);
                    options.inScaled = true;
                    if (fakeOption.outWidth / options.outWidth > fakeOption.outHeight / options.outHeight) {
                        options.inSampleSize = fakeOption.outWidth / options.outWidth;
                    } else {
                        options.inSampleSize = fakeOption.outHeight / options.outHeight;
                    }
                    inputStream = contentResolver.openInputStream(bmpUri);
                }
            } catch (FileNotFoundException e) {
                somethingWrong = true;
                if (defaultImgResId == 0) {
                    return Bitmap.createBitmap(options.outWidth == 0 ? 300: options.outWidth,
                                                options.outHeight == 0 ? 300: options.outHeight, Bitmap.Config.RGB_565);
                }else{
                    return null;
                }
            }
        }

        return BitmapFactory.decodeStream(inputStream,new Rect(0,0,options.outWidth,options.outHeight),options);
    }

    @Override
    protected void onProgressUpdate(Integer... values){

    }

    @Override
    protected void onPostExecute(Bitmap result){
        if (isCancelled()){
            result = null;
            return;
        }
        final ImageView imageView = imageViewWeakReference.get();
        final DPLTask dplTask =
                getBitmapWorkerTask(imageView);
        if (this == dplTask && imageView != null/* &&
                ((defaultImgResId !=0 && !somethingWrong) || (defaultImgResId == 0 && somethingWrong))*/) {
            if (cropped){
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            imageView.setImageBitmap(result);
            if (defaultImgResId ==0 && somethingWrong)
                return;
        }
        DPLRamCache.getStaticInstance().put(cacheKey,result);
        if (type == TASK_TYPE_URL && !DPLDiskCache.getStaticInstance().isCached(cacheKey)){
            DPLDiskCache.getStaticInstance().put(cacheKey,result);
        }
    }

    private static DPLTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DPLDrawable) {
                final DPLDrawable asyncDrawable = (DPLDrawable) drawable;
                return asyncDrawable.getTask();
            }
        }
        return null;
    }

    public void setOptions(BitmapFactory.Options options, boolean resized, boolean cropped){
        this.options = options;
        this.resized = resized;
        this.cropped = cropped;
    }

    public void setDefaultImgResId(int id){
        defaultImgResId = id;
    }
}
