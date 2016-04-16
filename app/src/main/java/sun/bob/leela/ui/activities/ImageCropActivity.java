package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.lyft.android.scissors.BitmapLoader;
import com.lyft.android.scissors.CropView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sun.bob.leela.R;
import sun.bob.leela.utils.EnvUtil;
import sun.bob.leela.utils.ResUtil;

public class ImageCropActivity extends AppCompatActivity {

    private String image;
    private CropView cropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Uri uri = getIntent().getData();
        if (uri.getScheme().equalsIgnoreCase("file")) {
            image = uri.getPath();
        } else {
            Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = getContentResolver().query(getIntent().getData(),  proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                image = cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }


        cropView = (CropView) findViewById(R.id.crop_view);
        BitmapLoader bitmapLoader = new BitmapLoader() {
            @Override
            public void load(Object model, ImageView view) {
                Picasso.with(ImageCropActivity.this)
                        .load("file://" + image)
                        .config(Bitmap.Config.RGB_565)
                        .into(view);
            }
        };
        cropView.extensions().using(bitmapLoader).load(getIntent().getData());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final File file = new File(EnvUtil.getInstance(null)
                        .getImageCropCacheFolder() + UUID.randomUUID().toString());
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    setResult(RESULT_CANCELED);
                    finish();
                    return;
                }
                final Future future = cropView.extensions()
                        .crop()
                        .quality(10)
                        .format(Bitmap.CompressFormat.JPEG)
                        .into(file);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            future.get(60, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } finally {
                            Intent data = new Intent();
                            data.putExtra("cacheIcon", file.getPath());
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    }
                }).start();

            }
        });
    }
}
