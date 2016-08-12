package sun.bob.leela.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;

import de.greenrobot.event.EventBus;
import sun.bob.leela.adapters.SettingsSpinnerAdapter;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.events.CryptoEvent;
import sun.bob.leela.events.DBExportEvent;
import sun.bob.leela.runnable.ChangePasswordRunnable;
import sun.bob.leela.runnable.DBExportRunnable;
import sun.bob.leela.ui.views.SelectorItem;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CryptoUtil;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.UserDefault;

/**
 * Created by bob.sun on 16/6/21.
 */
public class SettingsActivity extends MaterialSettings {

    public static final int RequestCodeSetMainPassword = 0x700;
    private TextItem quickSwitcher;
    private SelectorItem selectorItem;
    private AppCompatDialog dialog;
    private boolean didClickedChangeMaster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        EventBus.getDefault().register(this);

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

        didClickedChangeMaster = false;
        addItem(new TextItem(this, "change_password").setTitle("Change Master Password").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                didClickedChangeMaster = true;
                startActivity(new Intent(SettingsActivity.this, AuthorizeActivity.class));
            }
        }));

        addItem(new HeaderItem(this).setTitle("Database"));
        addItem(new TextItem(this, "database").setTitle("Export Database.")
                .setSubtitle("Database file can be used in other platforms")
                .setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {

                new AlertDialog.Builder(SettingsActivity.this).setTitle("Export Database")
                        .setMessage("Do you want to export database to external storage? ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                d.dismiss();
                                dialog = ResUtil.getInstance(null).showProgressbar(SettingsActivity.this);
                                DBExportRunnable runnable = new DBExportRunnable(SettingsActivity.this);
                                new Thread(runnable).run();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        }));

        addItem(new HeaderItem(this).setTitle("About"));
        addItem(new TextItem(this, "about").setTitle("About Puff").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        }));

//        if (UserDefault.getInstance(null).hasQuickPassword()) {
//            selectorItem = new SelectorItem(this, UserDefault.kSettingsQuickPassByte).setAdapter(new SettingsSpinnerAdapter()).setOnItemClickListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    getStorageInterface().save(UserDefault.kSettingsQuickPassByte, id);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//            if (UserDefault.getInstance(null).getQuickPassByte() == UserDefault.v4x4) {
//                selectorItem.setSelectedPos(1);
//            } else {
//                selectorItem.setSelectedPos(0);
//            }
//
//            addItem(selectorItem);
//        } else {
//
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        quickSwitcher.updateTitle(UserDefault.getInstance(null).hasQuickPassword() ? "Disable Gesture Lock" : "Enable Gesture Lock");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    public void onEventMainThread(Object event) {
//        if (!(event instanceof DBExportEvent)) {
//            return;
//        }
        if (event instanceof CryptoEvent && didClickedChangeMaster) {
            didClickedChangeMaster = false;
            if (((CryptoEvent) event).getType() == AppConstants.TYPE_MASTERPWD) {
                Intent intent = new Intent(SettingsActivity.this, SetMasterPasswordActivity.class);
                intent.putExtra("showMode", SetMasterPasswordActivity.ShowMode.ShowModeChange);
                intent.putExtra("oldPassword", ((CryptoEvent) event).getResult());
                UserDefault.getInstance(null).clearQuickPassword();
                AccountHelper.getInstance(null).clearQuickAccount();
                startActivity(intent);
            }
            return;
        }
        dialog.dismiss();
        DBExportEvent dbExportEvent = (DBExportEvent) event;
        if (dbExportEvent.success) {
            new AlertDialog.Builder(this).setTitle("Success!").setMessage("Database exported to " + dbExportEvent.filePath)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle("Failed!").setMessage("Please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
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
