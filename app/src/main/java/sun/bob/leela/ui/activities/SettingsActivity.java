package sun.bob.leela.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.storage.StorageInterface;

import sun.bob.leela.utils.UserDefault;

/**
 * Created by bob.sun on 16/6/21.
 */
public class SettingsActivity extends MaterialSettings {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        addItem(new HeaderItem(this).setTitle("Security"));
        addItem(new CheckboxItem(this, "launch_pass").setTitle("Need password when launch").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {

            }
        }));
    }

    @Override
    public StorageInterface initStorageInterface() {
        return UserDefault.getInstance(this);
    }


}
