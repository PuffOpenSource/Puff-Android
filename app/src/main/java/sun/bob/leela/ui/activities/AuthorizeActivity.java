package sun.bob.leela.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import sun.bob.leela.App;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.runnable.CryptoRunnable;
import sun.bob.leela.runnable.PBKDFRunnable;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CryptoUtil;
import sun.bob.leela.utils.ResUtil;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class AuthorizeActivity extends AppCompatActivity {

    // UI references.
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Account master;
    private AppCompatDialog dialog;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.

        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // TODO: 16/4/4 Check master password is correct or not.
            dialog = ResUtil.getInstance(this).showProgressbar(this);
            master = AccountHelper.getInstance(this).getMasterAccount();
//            new Thread(new CryptoRunnable(master.getHash(), password, AppConstants.TYPE_DECRYPT, "master"))
//                    .start();
            new Thread(new PBKDFRunnable(password, master.getHash())).start();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void onEventMainThread(CryptoEvent event) {
        if (event.getField() == null ||!event.getField().equalsIgnoreCase("master")) {
            return;
        }
        if (event.getType() == AppConstants.TYPE_MASTER_OK) {
            dialog.dismiss();
            CryptoEvent result = new CryptoEvent(password, AppConstants.TYPE_MASTERPWD);
            EventBus.getDefault().post(result);
            this.setResult(RESULT_OK);
            finish();
        }
        if (event.getType() == AppConstants.TYPE_MASTER_NO) {
            mPasswordView.setError(getResources().getString(R.string.master_password_invalid));
            dialog.dismiss();
        }
        if (event.getType() == AppConstants.TYPE_SHTHPPN) {
            mPasswordView.setError(getResources().getString(R.string.master_password_invalid));
            dialog.dismiss();
        }


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
        CryptoEvent result = new CryptoEvent("", AppConstants.TYPE_CANCELED);
        EventBus.getDefault().post(result);
        EventBus.getDefault().unregister(this);
        this.setResult(RESULT_CANCELED);
        finish();
    }
}

