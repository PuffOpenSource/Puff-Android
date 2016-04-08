package sun.bob.leela.runnable;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import de.greenrobot.event.EventBus;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/4/8.
 */
public class PBKDFRunnable implements Runnable {

    private String password;
    private String hash;
    private int RUN_MODE;

    public PBKDFRunnable(String password) {
        this.password = password;
        this.RUN_MODE = AppConstants.TYPE_ENCRYPT;
    }

    public PBKDFRunnable(String password, String hash) {
        this.password = password;
        this.hash = hash;
        this.RUN_MODE = AppConstants.TYPE_DECRYPT;
    }

    private String hash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 128;
        char[] chars = password.toCharArray();

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);


        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + Base64.encodeToString(salt, Base64.DEFAULT) + ":" + Base64.encodeToString(hash, Base64.DEFAULT);
    }

    private boolean validate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = hash.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.decode(parts[1], Base64.DEFAULT);
        byte[] hash = Base64.decode(parts[2], Base64.DEFAULT);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    @Override
    public void run() {
        CryptoEvent event = null;
        switch (RUN_MODE) {
            case AppConstants.TYPE_DECRYPT:
                try {
                    if (validate()) {
                        event = new CryptoEvent("", AppConstants.TYPE_MASTER_OK, "master");
                    } else {
                        event = new CryptoEvent("", AppConstants.TYPE_MASTER_NO, "master");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    event = new CryptoEvent("", AppConstants.TYPE_MASTER_NO, "master");
                } finally {
                    EventBus.getDefault().post(event);
                }
                break;
            case AppConstants.TYPE_ENCRYPT:
                try {
                    event = new CryptoEvent(hash(), AppConstants.TYPE_ENCRYPT, "master");
                } catch (Exception e) {
                    e.printStackTrace();
                    event = new CryptoEvent("", AppConstants.TYPE_SHTHPPN, "master");
                } finally {
                     EventBus.getDefault().post(event);
                }
                break;
            default:
                break;
        }
        EventBus.getDefault().unregister(this);
    }
}
