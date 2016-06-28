package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;

import sun.bob.leela.utils.CryptoUtil;
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

        addItem(new SwitcherItem(this, "quick_pass").setTitle("Enable Gesture Lock").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                Intent intent = new Intent(SettingsActivity.this, SetQuickPasswordActivity.class);
                intent.putExtra("type", SetQuickPasswordActivity.ShowTypeSet);
                startActivity(intent);
            }
        }));

        // TODO: 16/6/28 Delete below
        addItem(new SwitcherItem(this, "quick_pass").setTitle("Enable Gesture Lock").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                Intent intent = new Intent(SettingsActivity.this, SetQuickPasswordActivity.class);
                intent.putExtra("type", SetQuickPasswordActivity.ShowTypeVerify);
                startActivity(intent);
            }
        }));
    }

    @Override
    public StorageInterface initStorageInterface() {
        return UserDefault.getInstance(this);
    }


}
