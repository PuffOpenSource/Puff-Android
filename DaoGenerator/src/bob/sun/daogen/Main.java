package bob.sun.daogen;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        Schema schema = new Schema(1, "sun.bob.leela.db");
        addAcctItem(schema);
        addCategoryItem(schema);
        addAcctTypeItem(schema);
        try {
            DaoGenerator generator = new DaoGenerator();
            generator.generateAll(schema, "./gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAcctItem(Schema schema){
        Entity account = schema.addEntity("Account");
        account.addIdProperty();
        account.addStringProperty("name").notNull();
        account.addLongProperty("type").notNull();
        account.addStringProperty("account");
        account.addStringProperty("masked_account");
        account.addBooleanProperty("hide_name");
        account.addStringProperty("account_salt");
        account.addStringProperty("salt").notNull();
        account.addStringProperty("hash").notNull();
        account.addStringProperty("additional");
        account.addStringProperty("additional_salt");
        account.addLongProperty("category").notNull();
        account.addStringProperty("tag").notNull();
        account.addLongProperty("last_access");
        account.addStringProperty("icon");
        account.setHasKeepSections(true);
    }

    public void addCategoryItem(Schema schema) {
        Entity category = schema.addEntity("Category");
        category.addIdProperty();
        category.addStringProperty("name").notNull();
        category.addIntProperty("type").notNull();
        category.addStringProperty("icon").notNull();
        category.setHasKeepSections(true);
    }

    public void addAcctTypeItem(Schema schema) {
        Entity acctType = schema.addEntity("AcctType");
        acctType.addIdProperty();
        acctType.addStringProperty("name").notNull();
        acctType.addLongProperty("category").notNull();
        acctType.addBooleanProperty("numbers_only").notNull();
        acctType.addIntProperty("max_length").notNull();
        acctType.addStringProperty("icon");
        acctType.setHasKeepSections(true);
    }
}
