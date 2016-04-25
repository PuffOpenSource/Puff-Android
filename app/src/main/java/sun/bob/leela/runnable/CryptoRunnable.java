package sun.bob.leela.runnable;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.greenrobot.event.EventBus;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/1/25.
 */
public class CryptoRunnable implements Runnable {

    private String text, password, field;
    private byte[] rawText;
    private int runModel;

    public CryptoRunnable(String text, String password, int runModel, String field){
        this.text = text;
        this.password = password;
        this.runModel = runModel;
        this.field = field;
    }

    public CryptoRunnable setText(String text) {
        this.text = text;
        return this;
    }

    public CryptoRunnable setPassword(String password) {
        this.password = password;
        return this;
    }

    public CryptoRunnable setRunModel(int runModel) {
        this.runModel = runModel;
        return this;
    }

    public CryptoRunnable setField(String field) {
        this.field = field;
        return this;
    }

    @Override
    public void run() {
        CryptoEvent result = null;
        if (this.runModel == AppConstants.TYPE_ENCRYPT){
            try {
                byte[] rawRslt = encrypt();
                String rslt = Base64.encodeToString(rawRslt, Base64.DEFAULT);
                result = new CryptoEvent(rslt, AppConstants.TYPE_ENCRYPT, this.field);
            } catch (Exception e){
                e.printStackTrace();
                result = new CryptoEvent(e.getMessage(), AppConstants.TYPE_SHTHPPN, this.field);
            } finally {
                EventBus.getDefault().post(result);
            }
        } else if (this.runModel == AppConstants.TYPE_DECRYPT){
            try {
                this.rawText = Base64.decode(this.text, Base64.DEFAULT);
                String rslt = new String(decrypt());
                result = new CryptoEvent(rslt, AppConstants.TYPE_DECRYPT, this.field);
            } catch (Exception e){
                e.printStackTrace();
                result = new CryptoEvent(e.getMessage(), AppConstants.TYPE_SHTHPPN, this.field);
            } finally {
                EventBus.getDefault().post(result);
            }
        }
    }

    private byte[] encrypt() throws Exception{
        SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes("UTF-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return encrypted;
    }

    public byte[] decrypt() throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes("UTF-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(this.rawText);
        return decrypted;
    }
}
