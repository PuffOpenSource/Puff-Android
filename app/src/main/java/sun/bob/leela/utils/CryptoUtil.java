package sun.bob.leela.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;

import java.util.UUID;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountDao;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.runnable.CryptoRunnable;
import sun.bob.leela.ui.activities.AuthorizeActivity;

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

    public CryptoUtil(Context context) {
        this.context = context;
        init();
    }

    public CryptoUtil(Context context, OnEncryptedListener onEncryptedListener) {
        this.context = context;
        this.onEncryptedListener = onEncryptedListener;
        init();
    }

    public CryptoUtil(Context context, OnDecryptedListener onDecryptedListener) {
        this.context = context;
        this.onDecryptedListener = onDecryptedListener;
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
        return this;
    }

    public CryptoUtil setOnDecryptedListener(OnDecryptedListener onDecryptedListener) {
        this.onDecryptedListener = onDecryptedListener;
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

    public void runDecrypt() {
        Thread thread;
    }

    private void runEncrypt(String password) {
        // TODO: 16/4/4 Actual encrypt process.
        new Thread(new CryptoRunnable(this.passwd, password + salts[0], AppConstants.TYPE_ENCRYPT, "password")).start();
        new Thread(new CryptoRunnable(this.account, password + salts[1], AppConstants.TYPE_ENCRYPT, "account")).start();
        new Thread(new CryptoRunnable(this.addt, password + salts[2], AppConstants.TYPE_ENCRYPT, "addt")).start();

    }

    private void runDecrypt(String password) {

    }


    public interface OnEncryptedListener {
        void onEncrypted(String acctHash, String passwdHash, String addtHash,
                         String acctSalt, String passwdSalt, String addtSalt);
    }
    public interface OnDecryptedListener {

    }

    public void askForMasterPassword() {
        // TODO: 16/4/5 Un-comment below code!
        if (!AccountHelper.getInstance(this.context).hasMasterPassword()) {
            return;
        }
        Intent intent = new Intent(this.context, AuthorizeActivity.class);
        this.context.startActivity(intent);
        return;
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
                dialog.dismiss();
                EventBus.getDefault().unregister(this);
                break;
            case AppConstants.TYPE_ENCRYPT:
                // TODO: 16/4/4 Encrypted, save it & dismiss dialog.
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
                        break;
                }
                if (this.accountHash != null && this.passwdHash != null && this.addtHash != null) {
                    dialog.dismiss();
                    this.onEncryptedListener.onEncrypted(accountHash, passwdHash, addtHash,
                            salts[0], salts[1], salts[2]);
                }
                break;
            default:
                break;
        }
    }
}
