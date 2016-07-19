package sun.bob.leela;

import android.app.Application;

import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.db.TypeHelper;
import sun.bob.leela.utils.CategoryUtil;
import sun.bob.leela.utils.EnvUtil;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.UserDefault;

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
        EnvUtil.getInstance(getApplicationContext());
        UserDefault.getInstance(getApplicationContext());

    }
}
