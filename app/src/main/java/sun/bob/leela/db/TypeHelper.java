package sun.bob.leela.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.CategoryUtil;

/**
 * Created by bob.sun on 16/3/24.
 */
public class TypeHelper {
    private static TypeHelper ourInstance;
    private Context context;
    private DaoMaster daoMaster;
    private AcctTypeDao acctTypeDao;
    private DaoSession daoSession;

    public static TypeHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TypeHelper(context);
        }
        return ourInstance;
    }

    private TypeHelper(Context context) {
        this.context = context;
        getDaoMaster();
        getDaoSession();
        acctTypeDao = daoSession.getAcctTypeDao();
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

    public ArrayList<AcctType> getAllTypes (){
        return (ArrayList<AcctType>) acctTypeDao.loadAll();
    }

    public AcctType getTypeById(Long id) {
        List<AcctType> result = acctTypeDao.queryBuilder()
                .where(AcctTypeDao.Properties.Id.eq(id))
                .list();
        return result == null || result.size() == 0 ? null : result.get(0);
    }

    public AcctType getTypeByName(String name) {
        List<AcctType> result = acctTypeDao.queryBuilder()
                .where(AcctTypeDao.Properties.Name.eq(name))
                .list();
        return result == null || result.size() == 0 ? null : result.get(0);
    }

    public long save(AcctType acctType) {
        if (acctType.getId() == null || getTypeById(acctType.getId()) == null)
            return acctTypeDao.insertOrReplace(acctType);
        acctTypeDao.update(acctType);
        return acctType.getId();
    }

    public void delete(AcctType acctType) {
        acctTypeDao.delete(acctType);
    }
}
