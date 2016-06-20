package sun.bob.leela.runnable;

import android.util.Base64;
import android.util.Log;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/6/20.
 */
public class QuickPassRunnable implements Runnable {

    private String quickCode, password, passwordHash, passwordSalt;
    private int type;


    public QuickPassRunnable(String quickCode) {
        this.quickCode = quickCode;
        getPasswordHash();
        type = AppConstants.TYPE_DECRYPT;
    }

    public QuickPassRunnable(String quickCode, String password) {
        this.quickCode = quickCode;
        this.password = password;
        type = AppConstants.TYPE_ENCRYPT;
    }

    private void getPasswordHash() {
        // TODO: 16/6/20 Get password hash from db
        this.passwordHash = "128:PNkbKgdRSGDBQ3pU+QPqWLZBNj+SZz+2tk665J3vbFq21HX7Tf2Ledz/NIQ8vM9B:YJM4YeLk8FTIJiCqZMQkxw==:mn3RivWFHZ5v8T7eQiyr8A==";
    }

    @Override
    public void run() {
        switch (type) {
            case AppConstants.TYPE_DECRYPT:
                try {
                    decrypt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case AppConstants.TYPE_ENCRYPT:
                try {
                    encrypt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private String encrypt() throws Exception{

        int iterations = 128;

        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(quickCode.toCharArray(), salt, iterations,
                256);
        SecretKey secretKey = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();

        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        byte[] output = cipher.doFinal(password.getBytes());

        String ret = iterations + ":"
                + Base64.encodeToString(output, Base64.DEFAULT) + ":"
                + Base64.encodeToString(salt, Base64.DEFAULT) + ":"
                + Base64.encodeToString(iv, Base64.DEFAULT);
        Log.e("Encrypted Data ", ret);
        return ret;

    }

    public String decrypt() throws Exception {

        String[] parts = passwordHash.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] hash = Base64.decode(parts[1], Base64.DEFAULT);
        byte[] salt = Base64.decode(parts[2], Base64.DEFAULT);
        byte[] iv   = Base64.decode(parts[3], Base64.DEFAULT);

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(quickCode.toCharArray(), salt, iterations,
                256);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));


        byte[] output = cipher.doFinal(hash);

        String ret = new String(output);

        Log.e("Decrypted: ", ret);

        return ret;
    }
}
