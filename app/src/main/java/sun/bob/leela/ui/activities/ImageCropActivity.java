package sun.bob.leela.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.lyft.android.scissors.BitmapLoader;
import com.lyft.android.scissors.CropView;


import java.io.IOException;

import sun.bob.leela.R;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
