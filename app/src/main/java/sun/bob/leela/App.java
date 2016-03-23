package sun.bob.leela;

import android.app.Application;

import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.DeviceUtil;

/**
 * Created by bob.sun on 16/3/19.
 */
public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        AccountHelper helper = AccountHelper.getInstance(getApplicationContext());
        CategoryHelper.getInstance(getApplicationContext());
        DeviceUtil.getInstance(getApplicationContext());
        // TODO: 16/3/20 Debug code
        if (helper.getAllAccount() != null && helper.getAllAccount().size() > 0){
            return;
        }
        Account toAdd;
        for (int i = 0; i < 20; i ++){
            toAdd = new Account();
            toAdd.setAccount_name("widekuan@outlook.com");
            toAdd.setCategory(AppConstants.CAT_ID_DEFAULT);
            toAdd.setHash("ABCDEFG");
            toAdd.setName("Default" + i);
            toAdd.setTag("Default");
            toAdd.setSalt("HIJKLMN");
            helper.saveAccount(toAdd);
        }

        for (int i = 0; i < 20; i ++){
            toAdd = new Account();
            toAdd.setAccount_name("widekuan@outlook.com");
            toAdd.setCategory(AppConstants.CAT_ID_MAIL);
            toAdd.setHash("ABCDEFG");
            toAdd.setName("Mail"  + i);
            toAdd.setTag("Default");
            toAdd.setSalt("HIJKLMN");
            helper.saveAccount(toAdd);
        }

        for (int i = 0; i < 20; i ++){
            toAdd = new Account();
            toAdd.setAccount_name("widekuan@outlook.com");
            toAdd.setCategory(AppConstants.CAT_ID_SOCIAL);
            toAdd.setHash("ABCDEFG");
            toAdd.setName("Social"  + i);
            toAdd.setTag("Default");
            toAdd.setSalt("HIJKLMN");
            helper.saveAccount(toAdd);
        }
    }
}
