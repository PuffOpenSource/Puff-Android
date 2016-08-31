package sun.bob.leela.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import sun.bob.leela.R;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.EnvUtil;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;

/**
 * Created by bob.sun on 16/4/1.
 */
public class AddCategoryDialogActivity extends AppCompatActivity {

    private AppCompatButton buttonOK, buttonCancel;
    private ImageView imageView;
    private EditText etName;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_dialog);

        etName = (EditText) findViewById(R.id.id_name);
        buttonOK = (AppCompatButton) findViewById(R.id.button_ok);
        ViewCompat.setElevation(buttonOK, 10);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setResult();
                if (StringUtil.isNullOrEmpty(etName.getText().toString())) {
                    return;
                }
                if (StringUtil.isNullOrEmpty(imgPath)) {
                    return;
                }
                Category category = new Category();
                category.setName(etName.getText().toString());
                category.setIcon(imgPath);
                category.setType(AppConstants.CAT_TYPE_CUSTOM);
                CategoryHelper.getInstance(null).saveCategory(category);
                finish();
            }
        });

        buttonCancel = (AppCompatButton) findViewById(R.id.button_cancel);
        ViewCompat.setElevation(buttonCancel, 10);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(AddCategoryDialogActivity.this)
                        .setCancelable(false)
                        .setTitle(R.string.cancel_qm)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, AppConstants.REQUEST_CODE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AppConstants.REQUEST_CODE_IMAGE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                Uri imageUri = data.getData();
                Intent intent = new Intent(AddCategoryDialogActivity.this,
                        ImageCropActivity.class);
                intent.setData(imageUri);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_CROP);
                break;
            case AppConstants.REQUEST_CODE_CROP:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String file = data.getStringExtra("cacheIcon");
                imgPath = EnvUtil.getInstance(null).getCatIconFolder() + UUID.randomUUID().toString();
                FileOutputStream outputStream;
                FileInputStream inputStream;
                try {
                    outputStream = new FileOutputStream(imgPath);
                    inputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.close();
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    imgPath = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    imgPath = null;
                }
                Picasso.with(this).load(ResUtil.getInstance(null).getBmpUri(imgPath))
                        .fit()
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);
                new File(file).delete();
                break;
            default:
                break;
        }
    }
}
