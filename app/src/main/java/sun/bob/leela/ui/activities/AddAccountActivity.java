package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.adapters.CategorySpinnerAdapter;
import sun.bob.leela.adapters.TypeSpinnerAdapter;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.AcctType;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CryptoUtil;
import sun.bob.leela.utils.EnvUtil;
import sun.bob.leela.utils.RegExUtil;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;

public class AddAccountActivity extends AppCompatActivity {

    private AppCompatSpinner spinnerCategory, spinnerType;
    private Long type;
    private Long category;
    private EditText name, account, password, addtional;
    private AppCompatImageView imageView;
    private String iconPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);

        wireViews();

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String disPlayName;
                if (RegExUtil.isEmail(account.getText().toString())) {
                    disPlayName = StringUtil.getMaskedEmail(account.getText().toString());
                } else {
                    disPlayName = StringUtil.getMaskedPhoneNumber(account.getText().toString());
                }
                new CryptoUtil(AddAccountActivity.this, new CryptoUtil.OnEncryptedListener() {
                    @Override
                    public void onEncrypted(String acctHash, String passwdHash, String addtHash, String acctSalt, String passwdSalt, String addtSalt) {
                        Account account = new Account();
                        account.setName(name.getText().toString());
                        account.setAccount(acctHash);
                        account.setAccount_salt(acctSalt);
                        account.setHash(passwdHash);
                        account.setSalt(passwdSalt);
                        account.setAdditional(addtHash);
                        account.setAdditional_salt(addtSalt);
                        account.setMasked_account(disPlayName);
                        account.setType(((AcctType) spinnerType.getSelectedItem()).getId());
                        account.setCategory(((Category) spinnerCategory.getSelectedItem()).getId());
                        account.setTag("");
                        account.setIcon(StringUtil.isNullOrEmpty(iconPath) ? "" : iconPath);
                        AccountHelper.getInstance(AddAccountActivity.this).saveAccount(account);
                        finish();
                    }
                }).runEncrypt(account.getText().toString(), password.getText().toString(), addtional.getText().toString());

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerType = (AppCompatSpinner) findViewById(R.id.spinner_type);
        spinnerType.setPrompt("Type");
        spinnerType.setAdapter(new TypeSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line));
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || !(parent.getAdapter() instanceof TypeSpinnerAdapter)) {
                    return;
                }
                AcctType type = (AcctType) (parent.getAdapter()).getItem(position);
                AddAccountActivity.this.type = type.getId();
                CategoryHelper helper = CategoryHelper.getInstance(null);
                int catePos = helper.getAllCategory().indexOf(helper.getCategoryById(type.getCategory()));
                AddAccountActivity.this.spinnerCategory.setSelection(catePos);
                AddAccountActivity.this.category = type.getCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = Long.valueOf(-1);
            }
        });

        spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinner_category);
        spinnerCategory.setAdapter(new CategorySpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line));
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0) {
                    return;
                }
                Category category = (Category) parent.getAdapter().getItem(position);
                AddAccountActivity.this.category = category.getId();

                Uri icon = ResUtil.getInstance(null)
                        .getBmpUri( ((Category) ((CategorySpinnerAdapter)spinnerCategory.getAdapter()).getItem(position)).getIcon());
                Picasso.with(AddAccountActivity.this).load(icon).fit().config(Bitmap.Config.RGB_565).into(imageView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = Long.valueOf(-1);
            }
        });

        Uri icon = ResUtil.getInstance(null)
                .getBmpUri( ((Category) ((CategorySpinnerAdapter)spinnerCategory.getAdapter()).getItem(0)).getIcon());
        Picasso.with(this).load(icon)
                .fit()
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    public void onEventMainThread(CryptoEvent event){
        switch (event.getType()) {
            case AppConstants.TYPE_FINISHED:
                // TODO: 16/4/4 Dismiss self and pop up snack bar.
                break;
            default:
                break;
        }
    }

    private void wireViews() {
        name = (EditText) findViewById(R.id.id_name);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        addtional = (EditText) findViewById(R.id.additional);
        imageView = (AppCompatImageView) findViewById(R.id.account_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AppConstants.REQUEST_CODE_IMAGE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                Uri imageUri = data.getData();
                Intent intent = new Intent(AddAccountActivity.this,
                        ImageCropActivity.class);
                intent.setData(imageUri);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_CROP);
                break;
            case AppConstants.REQUEST_CODE_CROP:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String file = data.getStringExtra("cacheIcon");
                iconPath = EnvUtil.getInstance(null).getAcctIconFolder() + UUID.randomUUID().toString();
                FileOutputStream outputStream;
                FileInputStream inputStream;
                try {
                    outputStream = new FileOutputStream(iconPath);
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
                    iconPath = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    iconPath = null;
                }
                Picasso.with(this).load(ResUtil.getInstance(null).getBmpUri(iconPath))
                        .fit()
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);
                new File(file).delete();
                break;
            case AppConstants.REQUEST_CODE_ADD_CATE:
                break;
            default:
                break;
        }
    }
}
