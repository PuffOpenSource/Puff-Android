package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.runnable.ChangePasswordRunnable;
import sun.bob.leela.runnable.PBKDFRunnable;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.OtherUtil;
import sun.bob.leela.utils.ResUtil;

public class SetMasterPasswordActivity extends AppCompatActivity {

    public enum ShowMode {
        ShowModeAdd,
        ShowModeChange,
    }

    private AppCompatEditText passwd, confirm;
    private AppCompatImageView confirmImgView;
//    private AnimateCheckBox checkBox;
//    private AppCompatTextView checkBoxHint;
    private TextView helpText;
    private final String uuid = UUID.randomUUID().toString();
    private ShowMode showMode;
    private String oldPassword;
    private AppCompatDialog dialog;

    private static final int REQ_CODE_AUTH_MASTER   = 0x7001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_master_password);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);

        initReference();
        initListener();

        showMode = (ShowMode) getIntent().getSerializableExtra("showMode");

        if (showMode == ShowMode.ShowModeChange) {
            oldPassword = getIntent().getStringExtra("oldPassword");
        }

//        if (AccountHelper.getInstance(this).hasMasterPassword()) {
//            Intent intent = new Intent(this, AuthorizeActivity.class);
//            this.startActivityForResult(intent, REQ_CODE_AUTH_MASTER);
//        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String err = validatePassword();
                if (err != null){
                    OtherUtil.closeSoftInput(SetMasterPasswordActivity.this, passwd);
                    helpText.setText(err);
                    return;
                }
//                new Thread(new CryptoRunnable(uuid, passwd.getText().toString(), AppConstants.TYPE_ENCRYPT, "master")).start();
                if (showMode == ShowMode.ShowModeAdd){
                    new Thread(new PBKDFRunnable(passwd.getText().toString())).start();
                } else {
                    dialog = ResUtil.getInstance(null).showProgressbar(SetMasterPasswordActivity.this);
                    new Thread(new ChangePasswordRunnable(SetMasterPasswordActivity.this, oldPassword, passwd.getText().toString())).run();
                }

            }
        });
    }

    private String validatePassword(){
        // TODO: 16/4/5 Security validation.
        if (!passwd.getText().toString().equalsIgnoreCase(confirm.getText().toString())) {
            return getString(R.string.password_dont_match_em);
        }
        if (passwd.getText().length() < 6) {
             return getString(R.string.password_is_too_short_em);
        }

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish(){
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (showMode == ShowMode.ShowModeChange) {
            finish();
        }
    }

    public void onEventMainThread(CryptoEvent event) {
        if (!event.getField().equalsIgnoreCase("master")) {
            return;
        }
        if (showMode == ShowMode.ShowModeChange && event.getType() == AppConstants.TYPE_MASTER_CHANGE) {
            dialog.dismiss();
            finish();
            return;
        }
        if (event.getType() == AppConstants.TYPE_ENCRYPT) {
            Account account = AccountHelper.getInstance(this).getMasterAccount();
            if (account == null) {
                account = new Account();
            }
            account.setHash(event.getResult());
            account.setSalt("");
            account.setName("");
            account.setType(AppConstants.TYPE_MASTER);
            account.setCategory(AppConstants.CAT_ID_PRIVATE);
            account.setTag("");
            AccountHelper.getInstance(null).saveAccount(account);
            finish();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode != REQ_CODE_AUTH_MASTER)
            return;
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
    }

    private void initReference(){
        passwd = (AppCompatEditText) findViewById(R.id.password);
        confirm = (AppCompatEditText) findViewById(R.id.confirm);
        confirmImgView = (AppCompatImageView) findViewById(R.id.confirm_img);
//        checkBox = (AnimateCheckBox) findViewById(R.id.quick_checkbox);
//        checkBoxHint = (AppCompatTextView) findViewById(R.id.checkbox_hint);
        helpText = (TextView) findViewById(R.id.help_text);
    }

    private void initListener(){
        passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(confirm.getText().toString())) {
                    confirmImgView.setImageResource(R.drawable.ic_yes);
                } else {
                    confirmImgView.setImageResource(R.drawable.ic_no);
                }
            }
        });

        passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    helpText.setText(R.string.enter_main_password);
                }
            }
        });

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(passwd.getText().toString())) {
                    confirmImgView.setImageResource(R.drawable.ic_yes);
                } else {
                    confirmImgView.setImageResource(R.drawable.ic_no);
                }
            }
        });
        confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    helpText.setText(R.string.confirm_main_password);
                }
            }
        });

//        checkBoxHint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkBox.setChecked(!checkBox.isChecked());
//                helpText.setText("Quick Access will enable gesture lock. <b>But it will make the password database easier to be hack.</b>");
//            }
//        });
    }

}
