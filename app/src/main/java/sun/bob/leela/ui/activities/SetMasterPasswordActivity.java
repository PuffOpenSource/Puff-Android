package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.hanks.library.AnimateCheckBox;

import java.util.UUID;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.runnable.CryptoRunnable;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CryptoUtil;

public class SetMasterPasswordActivity extends AppCompatActivity {

    private AppCompatEditText passwd, confirm;
    private AppCompatImageView confirmImgView;
    private AnimateCheckBox checkBox;
    private AppCompatTextView checkBoxHint;
    private TextView helpText;
    private final String uuid = UUID.randomUUID().toString();

    private static final int REQ_CODE_AUTH_MASTER   = 0x7001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_master_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        initReference();
        initListener();

        if (AccountHelper.getInstance(this).hasMasterPassword()) {
            Intent intent = new Intent(this, AuthorizeActivity.class);
            this.startActivityForResult(intent, REQ_CODE_AUTH_MASTER);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePassword()){
                    return;
                }
                new Thread(new CryptoRunnable(uuid, passwd.getText().toString(), AppConstants.TYPE_ENCRYPT, "master")).start();
            }
        });
    }

    private boolean validatePassword(){
        // TODO: 16/4/5 Security validation.
        return passwd.getText().toString().equalsIgnoreCase(confirm.getText().toString());
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
    public void onBackPressed() {

    }

    public void onEventMainThread(CryptoEvent event) {
        if (!event.getField().equalsIgnoreCase("master")) {
            return;
        }
        if (event.getType() == AppConstants.TYPE_ENCRYPT) {
            Account account = AccountHelper.getInstance(this).getMasterAccount();
            if (account == null) {
                account = new Account();
            }
            account.setHash(event.getResult());
            account.setSalt(uuid);
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
        checkBox = (AnimateCheckBox) findViewById(R.id.quick_checkbox);
        checkBoxHint = (AppCompatTextView) findViewById(R.id.checkbox_hint);
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
                    helpText.setText("Enter Master Password");
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
                    helpText.setText("Confirm Master Password");
                }
            }
        });

        checkBoxHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                helpText.setText("Quick Access will enable gesture lock. <b>But it will make the password database easier to be hack.</b>");
            }
        });
    }
}
