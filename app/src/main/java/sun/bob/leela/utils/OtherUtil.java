package sun.bob.leela.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by bigflower on 2016/10/22.
 * <p>
 * this util is added by bigflower, there are some temporary methods here
 * just for avoiding the conflict .
 * <p>
 * so if bob find some method is appeared in his other util class, he can replace it.
 */

public class OtherUtil {


    public static void closeSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
