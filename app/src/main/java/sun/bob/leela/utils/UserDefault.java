package sun.bob.leela.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bob.sun on 16/3/19.
 */
public class UserDefault {
    private Context context;
    private static UserDefault ourInstance;
    private SharedPreferences sharedPreferences;

    public static UserDefault getInstance(Context context) {
        ourInstance = new UserDefault(context);
        return ourInstance;
    }

    private UserDefault(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserDefautPrivate", Context.MODE_PRIVATE);
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

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }
}
