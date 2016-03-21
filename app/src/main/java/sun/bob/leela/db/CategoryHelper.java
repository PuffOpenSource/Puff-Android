package sun.bob.leela.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/3/20.
 */
public class CategoryHelper {
    private static CategoryHelper ourInstance;
    private Context context;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CategoryDao categoryDao;

    public static CategoryHelper getInstance(Context context) {
        if (ourInstance == null){
            ourInstance = new CategoryHelper(context);
        }
        return ourInstance;
    }

    private CategoryHelper(Context context) {
        this.context = context;
        getDaoMaster();
        getDaoSession();
        categoryDao = daoSession.getCategoryDao();
        if (getAllCategory() == null || getAllCategory().size() == 0) {
            initBuiltInCategory();
        }
    }

    public DaoMaster getDaoMaster(){
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, AppConstants.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(){
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private void initBuiltInCategory(){
        Category def = new Category();
        def.setName("Default");
        def.setColor("#F57C00");
        def.setType(AppConstants.CAT_TYPE_BUILTIN);
        def.setId(AppConstants.CAT_ID_DEFAULT);

        Category mail = new Category();
        mail.setName("Mails");
        mail.setColor("#388E3C");
        mail.setType(AppConstants.CAT_TYPE_BUILTIN);
        mail.setId(AppConstants.CAT_ID_MAIL);

        Category social = new Category();
        social.setName("Social");
        social.setColor("#FF4081");
        social.setType(AppConstants.CAT_TYPE_BUILTIN);
        social.setId(AppConstants.CAT_ID_SOCIAL);

        saveCategory(def);
        saveCategory(mail);
        saveCategory(social);
    }

    public ArrayList<Category> getAllCategory() {
        return (ArrayList<Category>) categoryDao.loadAll();
    }

    public Category getCategoryByName(String name){
        List<Category> result = categoryDao.queryBuilder()
                        .where(CategoryDao.Properties.Name.eq(name))
                        .list();
        return result == null ? null : result.get(0);
    }

    public void saveCategory(Category category) {
        categoryDao.insertOrReplace(category);
    }

    public void deleteCategory(Category category) {
        categoryDao.delete(category);
    }
}
