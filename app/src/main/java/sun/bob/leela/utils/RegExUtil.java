package sun.bob.leela.utils;

import java.util.regex.Pattern;

/**
 * Created by bob.sun on 16/4/11.
 */
public class RegExUtil {


    public static String REG_EX_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static String REG_EX_SECURE_PASSWD = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z])(?!.*pass|.*word|.*1234|.*qwer|.*asdf).{6,}$";

    public static String REG_EX_SIMPE_WORDS = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z])";

    public static String REG_EX_SHORT = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z])(?!.*pass|.*word|.*1234|.*qwer|.*asdf).{,6}$";

    public static String REG_EX_NO_SPECIAL = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z])(?!.*pass|.*word|.*1234|.*qwer|.*asdf).{6,}$";

    public static String REG_EX_NO_NUMBER = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[a-z].*[a-z].*[a-z])(?!.*pass|.*word|.*1234|.*qwer|.*asdf).{6,}$";

    public static String REG_EX_NO_CAPS = "^(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z])(?!.*pass|.*word|.*1234|.*qwer|.*asdf).{6,}$";

    public static String REG_EX_NO_LOWERS = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?!.*pass|.*word|.*1234|.*qwer|.*asdf).{6,}$";

    public static boolean isEmail(String addr) {
        return Pattern.compile(REG_EX_EMAIL).matcher(addr).find();
    }

    public static boolean isSecurePassword(String password) {
        return false;
    }
}
