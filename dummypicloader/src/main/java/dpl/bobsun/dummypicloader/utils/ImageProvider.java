package dpl.bobsun.dummypicloader.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by bob.sun on 15/8/5.
 */
public class ImageProvider {
    private Context mContext;
    private ContentResolver mContentResolver;
    public ImageProvider(Context context){
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    public ArrayList<String> getAll(){
        ArrayList ret = new ArrayList();
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()){
            ret.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
        }
        return ret;
    }

}
