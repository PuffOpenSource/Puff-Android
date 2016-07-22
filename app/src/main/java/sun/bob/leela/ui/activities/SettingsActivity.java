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

    public static final int RequestCodeSetMainPassword = 0x700;
    private TextItem quickSwitcher;

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

        String title = UserDefault.getInstance(null).hasQuickPassword() ? "Disable Gesture Lock" : "Enable Gesture Lock";

        quickSwitcher = new TextItem(this, "quick_pass").setTitle(title).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                if (UserDefault.getInstance(null).hasQuickPassword()) {
                    UserDefault.getInstance(null).clearQuickPassword();
                    quickSwitcher.updateTitle("Enable Gesture Lock");
                } else {
                    Intent intent = new Intent(SettingsActivity.this, SetQuickPasswordActivity.class);
                    intent.putExtra("type", SetQuickPasswordActivity.ShowTypeSet);
                    startActivityForResult(intent, RequestCodeSetMainPassword);
                }
            }
        });
        addItem(quickSwitcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        quickSwitcher.updateTitle(UserDefault.getInstance(null).hasQuickPassword() ? "Disable Gesture Lock" : "Enable Gesture Lock");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RequestCodeSetMainPassword ) {
            return;
        }
        if (resultCode != RESULT_OK) {
            UserDefault.getInstance(null).clearQuickPassword();
        } else {
            UserDefault.getInstance(null).setHasQuickPassword();
        }
    }

    @Override
    public StorageInterface initStorageInterface() {
        return UserDefault.getInstance(this);
    }


}
