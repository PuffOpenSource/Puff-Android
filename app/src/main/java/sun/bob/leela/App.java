package sun.bob.leela;

import android.app.Application;

import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.db.TypeHelper;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CategoryUtil;
import sun.bob.leela.utils.ResUtil;

/**
 * Created by bob.sun on 16/3/19.
 */
public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        AccountHelper.getInstance(getApplicationContext());
        CategoryHelper categoryHelper = CategoryHelper.getInstance(getApplicationContext());
        TypeHelper typeHelper = TypeHelper.getInstance(getApplicationContext());

        if (categoryHelper.getAllCategory() == null || categoryHelper.getAllCategory().size() == 0) {
            CategoryUtil.getInstance(getApplicationContext()).initBuiltInCategories();
        }

        if (typeHelper.getAllTypes() == null || typeHelper.getAllTypes().size() == 0) {
            CategoryUtil.getInstance(getApplicationContext()).initBuiltInTypes();
        }

        ResUtil.getInstance(getApplicationContext());
        CategoryUtil.getInstance(getApplicationContext());
        // TODO: 16/3/20 Debug code
//        if (helper.getAllAccount() != null && helper.getAllAccount().size() > 0){
//            return;
//        }
//        Account toAdd;
//        for (int i = 0; i < 20; i ++){
//            toAdd = new Account();
//            toAdd.setAccount_name("widekuan@outlook.com");
//            toAdd.setCategory(AppConstants.CAT_ID_DEFAULT);
//            toAdd.setHash("ABCDEFG");
//            toAdd.setName("Default" + i);
//            toAdd.setTag("Default");
//            toAdd.setSalt("HIJKLMN");
//            helper.saveAccount(toAdd);
//        }
//
//        for (int i = 0; i < 20; i ++){
//            toAdd = new Account();
//            toAdd.setAccount_name("widekuan@outlook.com");
//            toAdd.setCategory(AppConstants.CAT_ID_MAIL);
//            toAdd.setHash("ABCDEFG");
//            toAdd.setName("Mail"  + i);
//            toAdd.setTag("Default");
//            toAdd.setSalt("HIJKLMN");
//            helper.saveAccount(toAdd);
//        }
//
//        for (int i = 0; i < 20; i ++){
//            toAdd = new Account();
//            toAdd.setAccount_name("widekuan@outlook.com");
//            toAdd.setCategory(AppConstants.CAT_ID_SOCIAL);
//            toAdd.setHash("ABCDEFG");
//            toAdd.setName("Social"  + i);
//            toAdd.setTag("Default");
//            toAdd.setSalt("HIJKLMN");
//            helper.saveAccount(toAdd);
//        }
    }
}
