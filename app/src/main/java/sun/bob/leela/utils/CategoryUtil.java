package sun.bob.leela.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import sun.bob.leela.db.AcctType;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.db.TypeHelper;

/**
 * Created by bob.sun on 16/3/24.
 */
public class CategoryUtil {
    private static CategoryUtil ourInstance;
    private Context context;

    public static CategoryUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new CategoryUtil(context);
        }
        return ourInstance;
    }

    private CategoryUtil(Context context) {
        this.context = context;
    }

    public void initBuiltInCategories(){
        CategoryHelper helper = CategoryHelper.getInstance(context);
        Category cat = new Category();

        cat.setName("Recent");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_RECENT);
        cat.setIcon("icon_category/cat_recent.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Cards");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_CARDS);
        cat.setIcon("icon_category/cat_cards.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Computers");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_COMPUTERS);
        cat.setIcon("icon_category/cat_computer.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Device");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_DEVICE);
        cat.setIcon("icon_category/cat_device.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Entry");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_ENTRY);
        cat.setIcon("icon_category/cat_entry.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Mail");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_MAIL);
        cat.setIcon("icon_category/cat_mail.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Social");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_SOCIAL);
        cat.setIcon("icon_category/cat_social.png");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Website");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_WEBSITE);
        cat.setIcon("icon_category/cat_website.png");
        helper.saveCategory(cat);
    }

    public void initBuiltInTypes (){
        TypeHelper helper = TypeHelper.getInstance(context.getApplicationContext());
        AcctType toAdd;
        try {
            for (String file : context.getAssets().list("cat_cards")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_CARDS);
                    toAdd.setIcon("cat_cards/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_computers")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_COMPUTERS);
                    toAdd.setIcon("cat_computers/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_device")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_DEVICE);
                    toAdd.setIcon("cat_device/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_mail")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_MAIL);
                    toAdd.setIcon("cat_mail/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_cards")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_MAIL);
                    toAdd.setIcon("cat_cards/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_entry")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_ENTRY);
                    toAdd.setIcon("cat_entry/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_social")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_SOCIAL);
                    toAdd.setIcon("cat_social/" + file);
                    helper.save(toAdd);
                }
            }

            for (String file : context.getAssets().list("cat_website")) {
                if (file.endsWith(".png")) {
                    toAdd = new AcctType();
                    toAdd.setName(file.replace(".png", ""));
                    toAdd.setCategory(AppConstants.CAT_ID_WEBSITE);
                    toAdd.setIcon("cat_website/" + file);
                    helper.save(toAdd);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
