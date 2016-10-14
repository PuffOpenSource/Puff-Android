package sun.bob.leela.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;

import java.util.UUID;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountDao;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.runnable.CryptoRunnable;
import sun.bob.leela.ui.activities.AuthorizeActivity;
import sun.bob.leela.ui.activities.SetQuickPasswordActivity;

/**
 * Created by bob.sun on 16/4/3.
 */
public class CryptoUtil {

    private Context context;
    private OnEncryptedListener onEncryptedListener;
    private OnDecryptedListener onDecryptedListener;
    private String account, passwd, addt, accountHash, passwdHash, addtHash;
    private String[] salts;
    private AppCompatDialog dialog;
    private int type;

    public CryptoUtil(Context context, OnEncryptedListener onEncryptedListener) {
        this.context = context;
        this.onEncryptedListener = onEncryptedListener;
        this.type = AppConstants.TYPE_ENCRYPT;
        init();
    }

    public CryptoUtil(Context context, OnDecryptedListener onDecryptedListener) {
        this.context = context;
        this.onDecryptedListener = onDecryptedListener;
        this.type = AppConstants.TYPE_DECRYPT;
        init();
    }

    private void init(){
        EventBus.getDefault().register(this);
        salts = new String[3];
        for(int i = 0; i < salts.length; i++) {
            salts[i] = UUID.randomUUID().toString();
        }
    }

    public CryptoUtil setOnEncryptedListener(OnEncryptedListener onEncryptedListener) {
        this.onEncryptedListener = onEncryptedListener;
        this.type = AppConstants.TYPE_ENCRYPT;
        return this;
    }

    public CryptoUtil setOnDecryptedListener(OnDecryptedListener onDecryptedListener) {
        this.onDecryptedListener = onDecryptedListener;
        this.type = AppConstants.TYPE_DECRYPT;
        return this;
    }

    public void runEncrypt(String account, String password, String addtional) {
        /**
         * To encrypt an account information, we need -
         * i. Hash Account name
         * ii. Hash Password
         * iii. Hash Additional Code if exists.
         */
        //Wait for main password.
        assert this.onEncryptedListener != null;
        this.account = account;
        this.passwd = password;
        this.addt = addtional;
        askForMasterPassword();
    }

    public void runDecrypt(String account, String password, String additional,
                           String acctSlt, String psswdSlt, String addtSalt) {
        assert this.onDecryptedListener != null;
        this.accountHash = account;
        this.passwdHash = password;
        this.addtHash = additional;
        this.salts[0] = acctSlt;
        this.salts[1] = psswdSlt;
        this.salts[2] = addtSalt;
        askForMasterPassword();
    }

    private void runEncrypt(String password) {
        // TODO: 16/4/4 Actual encrypt process.
        new Thread(new CryptoRunnable(this.account + salts[0], password, AppConstants.TYPE_ENCRYPT, "account")).start();
        new Thread(new CryptoRunnable(this.passwd + salts[1], password, AppConstants.TYPE_ENCRYPT, "password")).start();
        new Thread(new CryptoRunnable(this.addt + salts[2], password, AppConstants.TYPE_ENCRYPT, "addt")).start();

    }

    private void runDecrypt(String password) {
        new Thread(new CryptoRunnable(this.accountHash, password, AppConstants.TYPE_DECRYPT, "account")).start();
        new Thread(new CryptoRunnable(this.passwdHash, password, AppConstants.TYPE_DECRYPT, "password")).start();
        new Thread(new CryptoRunnable(this.addtHash, password, AppConstants.TYPE_DECRYPT, "addt")).start();
    }


    public interface OnEncryptedListener {
        void onEncrypted(String acctHash, String passwdHash, String addtHash,
                         String acctSalt, String passwdSalt, String addtSalt);
    }
    public interface OnDecryptedListener {
        void onDecrypted(String account, String passwd, String addt);
    }

    public void askForMasterPassword() {
        if (!AccountHelper.getInstance(this.context).hasMasterPassword()) {
            return;
        }

        if (UserDefault.getInstance(this.context).hasQuickPassword()) {
            Intent intent = new Intent(this.context, SetQuickPasswordActivity.class);
            intent.putExtra("type", SetQuickPasswordActivity.ShowTypeVerify);
            this.context.startActivity(intent);

        } else {
            Intent intent = new Intent(this.context, AuthorizeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            this.context.getApplicationContext().startActivity(intent);
        }
    }

    public void onEventMainThread(CryptoEvent event) {
        switch (event.getType()) {
            case AppConstants.TYPE_MASTERPWD:
                if (type == AppConstants.TYPE_DECRYPT) {
                    this.dialog = ResUtil.getInstance(null).showProgressbar((Activity) context);
                    runDecrypt(event.getResult());
                } else {
                    this.dialog = ResUtil.getInstance(null).showProgressbar((Activity) context);
                    runEncrypt(event.getResult());
                }
                break;
            case AppConstants.TYPE_DECRYPT:
                // TODO: 16/4/4 Decrypted, dismiss dialog & publish event.
                switch (event.getField()) {
                    case "account":
                        this.account = event.getResult();
                        break;
                    case "password":
                        this.passwd = event.getResult();
                        break;
                    case "addt":
                        this.addt = event.getResult();
                        break;
                    default:
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        return;
                }
                if (this.account != null && this.passwd != null && this.addt != null) {
//                    Log.e("LEELA", "Decrypted Callback");
                    account = account.replace(salts[0], "");
                    passwd = passwd.replace(salts[1], "");
                    addt = addt.replace(salts[2], "");
                    this.onDecryptedListener.onDecrypted(account, passwd, addt);
                    EventBus.getDefault().unregister(this);
                }
                dialog.dismiss();
                break;
            case AppConstants.TYPE_ENCRYPT:
                if (event.getField() == null) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    return;
                }
                switch (event.getField()) {
                    case "account":
                        this.accountHash = event.getResult();
                        break;
                    case "password":
                        this.passwdHash = event.getResult();
                        break;
                    case "addt":
                        this.addtHash = event.getResult();
                        break;
                    default:
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        return;
                }
                if (this.accountHash != null && this.passwdHash != null && this.addtHash != null) {
                    dialog.dismiss();
                    this.onEncryptedListener.onEncrypted(accountHash, passwdHash, addtHash,
                            salts[0], salts[1], salts[2]);
                    EventBus.getDefault().unregister(this);
                }
                break;
            case AppConstants.TYPE_CANCELED:
                EventBus.getDefault().unregister(this);
                break;
            case AppConstants.TYPE_SHTHPPN:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            default:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
        }
    }


}
