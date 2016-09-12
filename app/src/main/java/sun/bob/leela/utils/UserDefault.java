package sun.bob.leela.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kenumir.materialsettings.storage.StorageInterface;

import java.util.Map;

/**
 * Created by bob.sun on 16/3/19.
 */
public class UserDefault extends StorageInterface {
    private Context context;
    private static UserDefault ourInstance;
    private SharedPreferences sharedPreferences;

    public static UserDefault getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new UserDefault(context);
        return ourInstance;
    }

    private UserDefault(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserDefaultPrivate", Context.MODE_PRIVATE);
    }

    public UserDefault writeInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
        return this;
    }

    public UserDefault writeString(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
        return this;
    }

    public UserDefault writeBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
        return this;
    }

    public boolean hasQuickPassword() {
        return sharedPreferences.getBoolean(kSettingsHasQuickPassword, false);
    }

    public void setHasQuickPassword() {
        sharedPreferences.edit().putBoolean(kSettingsHasQuickPassword, true).commit();
    }

    public void clearQuickPassword() {
        sharedPreferences.edit().putBoolean(kSettingsHasQuickPassword, false).commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getAutoClearTimeInSeconds() {
        return 60;
    }

    @Override
    public void save(String s, Boolean aBoolean) {
        sharedPreferences.edit().putBoolean(s, aBoolean).commit();
    }

    @Override
    public boolean load(String s, Boolean aBoolean) {
        return sharedPreferences.getBoolean(s, false);
    }

    @Override
    public void save(String s, String s1) {
        sharedPreferences.edit().putString(s, s1).commit();
    }

    @Override
    public String load(String s, String s1) {
        return sharedPreferences.getString(s, s1);
    }

    @Override
    public void save(String s, Integer integer) {
        sharedPreferences.edit().putInt(s, integer).commit();
    }

    @Override
    public Integer load(String s, Integer integer) {
        return sharedPreferences.getInt(s, integer);
    }

    @Override
    public void save(String s, Float aFloat) {
        sharedPreferences.edit().putFloat(s, aFloat).commit();
    }

    @Override
    public Float load(String s, Float aFloat) {
        return sharedPreferences.getFloat(s, aFloat);
    }

    @Override
    public void save(String s, Long aLong) {
        sharedPreferences.edit().putLong(s, aLong).commit();
    }

    @Override
    public Long load(String s, Long aLong) {
        return sharedPreferences.getLong(s, aLong);
    }

    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public long getQuickPassByte() {
        return sharedPreferences.getLong(kSettingsQuickPassByte, v4x4);
    }

    public void setQuickPassByte(long b) {
        sharedPreferences.edit().putLong(kSettingsQuickPassByte, b).commit();
    }

    public void setNeedPasswordWhenLaunch(boolean need) {
        sharedPreferences.edit().putBoolean(kNeedPasswordWhenLaunch, need).commit();
    }

    public boolean needPasswordWhenLaunch() {
        return sharedPreferences.getBoolean(kNeedPasswordWhenLaunch, false);
    }

    public static final String kSettingsHasQuickPassword    = "kSettingsHasQuickPassword";
    public static final String kSettingsQuickPassByte       = "kSettingsQuickPassByte";
    public static final String kNeedPasswordWhenLaunch      = "kNeedPasswordWhenLaunch";
    public static final long v3x3                           = 0x1033;
    public static final long v4x4                           = 0x1044;
}
