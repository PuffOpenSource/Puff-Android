package sun.bob.leela.runnable;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.greenrobot.event.EventBus;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/8/12.
 */
public class ChangePasswordRunnable implements Runnable {

    private Context context;
    private ArrayList<Account> accounts;
//    private Stack<Account> accounts;
    private String oldPassword, newPassword;
    public ChangePasswordRunnable(Context context, String oldPassword, String newPassword) {
        this.context = context;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;

        EventBus.getDefault().register(this);

        accounts = AccountHelper.getInstance(null).getAllAccount();
    }
    @Override
    public void run() {
        CryptoEvent result = null;
        new PBKDFRunnable(newPassword.toString()).run();
        try {
            for (Account account : accounts) {
                if (account.getType() == AppConstants.TYPE_QUICK || account.getType() == AppConstants.TYPE_MASTER)
                    continue;

                    String acct = new String(decrypt(Base64.decode(account.getAccount(), Base64.DEFAULT)));//.replace(account.getAccount_salt(), "");
                    String passwd = new String(decrypt(Base64.decode(account.getHash(), Base64.DEFAULT)));//.replace(account.getSalt(), "");
                    String addt = new String(decrypt(Base64.decode(account.getAdditional(), Base64.DEFAULT)));//.replace(account.getAdditional_salt(), "");

                    account.setAccount(Base64.encodeToString(encrypt(acct), Base64.DEFAULT));
                    account.setHash(Base64.encodeToString(encrypt(passwd), Base64.DEFAULT));
                    account.setAdditional(Base64.encodeToString(encrypt(addt), Base64.DEFAULT));

                    AccountHelper.getInstance(null).saveAccount(account);
            }
            result = new CryptoEvent("", AppConstants.TYPE_MASTER_CHANGE, "master");
        } catch (Exception e) {
            e.printStackTrace();
            result = new CryptoEvent("", AppConstants.TYPE_SHTHPPN, "master");
        } finally {
            EventBus.getDefault().post(result);
        }

    }

    private byte[] encrypt(String text) throws Exception{
        SecretKeySpec skeySpec = new SecretKeySpec(newPassword.getBytes("UTF-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return encrypted;
    }

    public byte[] decrypt(byte[] rawText) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(oldPassword.getBytes("UTF-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(rawText);
        return decrypted;
    }

    public void onEventMainThread(CryptoEvent event) {
        if (!event.getField().equalsIgnoreCase("master")) {
            return;
        }
        if (event.getType() == AppConstants.TYPE_ENCRYPT) {
            Account account = AccountHelper.getInstance(null).getMasterAccount();
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
            EventBus.getDefault().unregister(this);
        }
    }
}
