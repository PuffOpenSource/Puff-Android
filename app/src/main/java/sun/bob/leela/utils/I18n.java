package sun.bob.leela.utils;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by bob.sun on 16/9/5.
 * This tool is for some in app string which can not translated by xml.
 */
public class I18n {
    private HashMap<Locale, HashMap> i18nTable;
    private static I18n instance;
    private Locale locale;

    public static I18n instance(Context ctx) {
        if (instance == null) {
            instance = new I18n(ctx);
        }
        return instance;
    }

    private I18n(Context context) {
        locale = context.getResources().getConfiguration().locale;
        i18nTable = new HashMap<>();
    }

    public String getStringForCurrentLocale(String str) {
        return (String) i18nTable.get(locale).get(str);
    }

    private void initForCN() {
        HashMap<String, String> map = new HashMap<>();
        //TL; DR
        i18nTable.put(Locale.CHINA, map);
        i18nTable.put(Locale.CHINESE, map);
    }

    private void initForEN() {

    }
}
