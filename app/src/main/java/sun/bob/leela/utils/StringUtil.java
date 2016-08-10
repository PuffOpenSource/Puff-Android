package sun.bob.leela.utils;

import android.util.Log;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
        ret = getStars(phone.substring(0, phone.length() - 4)) + ret;
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

    public static boolean isNullOrEmpty(String ... strs) {
        for (String s : strs) {
            if (!isNullOrEmpty(s))
                return false;
        }
        return true;
    }

    public static HashMap<String, ArrayList> charMap = null;

    private static HashMap getCharMap() {
        if (charMap != null) {
            return charMap;
        }
        charMap = new HashMap<>();
        ArrayList add = new ArrayList();
        add.add("@");
        add.add("4");
        charMap.put("A",add);
        add = new ArrayList();
        add.add("8");
        charMap.put("B", add);

        add = new ArrayList();
        add.add("3");
        charMap.put("E", add);

        add = new ArrayList();
        add.add("6");
        charMap.put("G", add);

        add = new ArrayList();
        add.add("#");
        charMap.put("H", add);

        add = new ArrayList();
        add.add("!");
        add.add("1");
        charMap.put("I", add);

        add = new ArrayList();
        add.add("0");
        charMap.put("O", add);

        add = new ArrayList();
        add.add("9");
        charMap.put("Q", add);

        add = new ArrayList();
        add.add("5");
        add.add("$");
        charMap.put("S", add);

        add = new ArrayList();
        add.add("7");
        charMap.put("T", add);

        add = new ArrayList();
        add.add("\\/");
        charMap.put("V", add);

        add = new ArrayList();
        add.add("2");
        charMap.put("Z", add);

        return charMap;
    }

    public static String getMaskedWord(String word) {
        word = word.toUpperCase();
        char[] chars = word.toCharArray();
        StringBuilder ret = new StringBuilder();
        HashMap<String, ArrayList> map = getCharMap();
        for (char c : chars) {
            if (getRandBool()) {
                if (getRandBool())
                    c = Character.toLowerCase(c);
                ret.append(c);
                continue;
            }
            ArrayList<String> masks = map.get(String.valueOf(c));
            if (masks == null || masks.size() == 0){
                if (getRandBool())
                    c = Character.toLowerCase(c);
                ret.append(c);
            } else {
                ret.append(masks.get(getRandInt(masks.size())));
            }
        }
        return ret.toString();
    }

    public static boolean getRandBool() {
        SecureRandom random = new SecureRandom();
        return random.nextBoolean();
    }

    public static int getRandInt(int range) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(range);
    }

    public static String timeStampToTime(String timeStamp) {
        if (isNullOrEmpty(timeStamp)) {
            return "--:--:--";
        }
        Long l = Long.valueOf(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(l));
    }

}
