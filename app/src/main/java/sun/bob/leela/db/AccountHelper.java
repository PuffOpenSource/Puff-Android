package sun.bob.leela.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
        if (account.getId() == null || getAccount(account.getId()) == null)
            return accountDao.insertOrReplace(account);
        accountDao.update(account);
        return account.getId();
    }

    public void deleteAccount(long id) {
        accountDao.deleteByKey(id);
    }

    public void deleteAccount(Account account) {
        accountDao.delete(account);
    }

    public boolean hasMasterPassword() {
        List result = accountDao.queryBuilder()
                .where(AccountDao.Properties.Type.eq(AppConstants.TYPE_MASTER))
                .list();
        return result.size() != 0;
    }

    public boolean hasQuickAccess() {
        List result = accountDao.queryBuilder()
                .where(AccountDao.Properties.Type.eq(AppConstants.TYPE_QUICK))
                .list();
        return result.size() != 0;
    }

    public Account getMasterAccount() {
        List result = accountDao.queryBuilder()
                .where(AccountDao.Properties.Type.eq(AppConstants.TYPE_MASTER))
                .list();
        return result.size() == 0 ? null : (Account) result.get(0);
    }

    public Account getQuickAccount() {
        List result = accountDao.queryBuilder()
                .where(AccountDao.Properties.Type.eq(AppConstants.TYPE_QUICK))
                .list();
        return result.size() == 0 ? null : (Account) result.get(0);
    }

    public void clearQuickAccount() {
        accountDao.queryBuilder()
                .where(AccountDao.Properties.Type.eq(AppConstants.TYPE_QUICK))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    public ArrayList<Account> getAccountsByCategory(Long category) {
        return (ArrayList) accountDao.queryBuilder()
                .where(AccountDao.Properties.Category.eq(category),
                        AccountDao.Properties.Type.notEq(AppConstants.TYPE_MASTER),
                        AccountDao.Properties.Type.notEq(AppConstants.TYPE_QUICK))
                        .orderDesc(AccountDao.Properties.Id)
                        .list();
    }

    public  ArrayList<Account> getRecentUsed(int limit) {
        return (ArrayList) accountDao.queryBuilder()
                .where(AccountDao.Properties.Type.notEq(AppConstants.TYPE_MASTER),
                        AccountDao.Properties.Type.notEq(AppConstants.TYPE_QUICK))
                .orderDesc(AccountDao.Properties.Last_access)
                .limit(limit)
                .list();

    }
}
