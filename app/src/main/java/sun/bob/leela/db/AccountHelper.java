package sun.bob.leela.db;

import android.content.Context;

import java.util.ArrayList;

import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/3/20.
 */
public class AccountHelper {
    private static AccountHelper ourInstance;
    private Context context;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AccountDao accountDao;

    public static AccountHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AccountHelper(context);
        }
        return ourInstance;
    }

    private AccountHelper(Context context) {
        this.context = context;
        getDaoMaster();
        getDaoSession();
        accountDao = daoSession.getAccountDao();
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

    public Account getAccount(long id) {
        return accountDao.load(id);
    }

    public ArrayList<Account> getAllAccount() {
        return (ArrayList<Account>) accountDao.loadAll();
    }

    public ArrayList<Account> queryAccount(String where,String ... params) {
        return (ArrayList<Account>) accountDao.queryRaw(where, params);
    }

    public long saveAccount(Account account) {
        return accountDao.insertOrReplace(account);
    }

    public void deleteAccount(long id) {
        accountDao.deleteByKey(id);
    }

    public void deleteAccount(Account account) {
        accountDao.delete(account);
    }

    public ArrayList<Account> getAccountsByCategory(Long category) {
        return (ArrayList) accountDao.queryBuilder()
                .where(AccountDao.Properties.Category.eq(category),
                        AccountDao.Properties.Type.eq(AppConstants.TYPE_NORMAL))
                        .orderDesc(AccountDao.Properties.Id)
                        .list();
    }
}
