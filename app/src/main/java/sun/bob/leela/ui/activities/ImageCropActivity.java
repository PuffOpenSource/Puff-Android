package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.lyft.android.scissors.BitmapLoader;
import com.lyft.android.scissors.CropView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sun.bob.leela.R;
import sun.bob.leela.utils.EnvUtil;

public class ImageCropActivity extends AppCompatActivity {

    private Bitmap image;
    private CropView cropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        cropView = (CropView) findViewById(R.id.crop_view);
        BitmapLoader bitmapLoader = new BitmapLoader() {
            @Override
            public void load(Object model, ImageView view) {
                //TODD Add DPL here.
                view.setImageBitmap((Bitmap) model);
            }
        };
        cropView.extensions().using(bitmapLoader).load(image);

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
                        .quality(50)
                        .format(Bitmap.CompressFormat.PNG)
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
