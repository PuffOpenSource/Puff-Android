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

        cat.setName("Default");
        cat.setColor("#F57C00");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_DEFAULT);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Cards");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_CARDS);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Computers");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_COMPUTERS);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Device");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_DEVICE);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Entry");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_ENTRY);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Mail");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_MAIL);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Social");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_SOCIAL);
        cat.setIcon("");
        helper.saveCategory(cat);

        cat = new Category();
        cat.setName("Website");
        cat.setColor("");
        cat.setType(AppConstants.CAT_TYPE_BUILTIN);
        cat.setId(AppConstants.CAT_ID_WEBSITE);
        cat.setIcon("");
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
