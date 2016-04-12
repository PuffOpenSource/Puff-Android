package sun.bob.leela.utils;

/**
 * Created by bob.sun on 16/4/11.
 */
public class StringUtil {

    public static String getMaskedEmail(String email) {
        if (isNullOrEmpty(email))
            return "";
        return email.replaceAll("(?<=.).(?=[^@]*?.@)", "*");
    }

    public static String getMaskedPhoneNumber(String phone) {
        if (isNullOrEmpty(phone)) {
            return "";
        }
        String ret;
        if (phone.length() < 4) {
            return phone.substring(0, 1) + getStars(phone.substring(1));
        }
        ret = phone.substring(phone.length() - 4, phone.length());
        ret += getStars(phone.substring(0, phone.length() - 4));
        return ret;
    }

    public static String getStars(String src) {
        if (isNullOrEmpty(src))
            return "";
        return src.replaceAll("\\S", "*");
    }

    public static String getStarsIgnoreWhite(String src) {
        if (isNullOrEmpty(src))
            return "";
        return src.replaceAll("\\s", "*");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str);
    }
}
