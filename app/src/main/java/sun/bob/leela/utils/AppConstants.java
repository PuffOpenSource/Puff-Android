package sun.bob.leela.utils;

/**
 * Created by bob.sun on 16/1/25.
 */
public class AppConstants {

    public static final Boolean debug = true;

    public static final String DB_NAME = "account.db";

    //Events Types
    public static final int TYPE_ENCRYPT = 0x233;
    public static final int TYPE_DECRYPT = 0x234;
    public static final int TYPE_SHTHPPN = 0x235;
    public static final int TYPE_MASTERPWD  = 0x236;
    public static final int TYPE_FINISHED = 0x257;

    //Category Type
    //Only custom type category can be deleted.
    public static final int CAT_TYPE_BUILTIN = 0x100;
    public static final int CAT_TYPE_CUSTOM  = 0x101;

    //Builtin Category ID
    public static final long CAT_ID_PRIVATE  = -1;
    public static final long CAT_ID_DEFAULT  = 1;
    public static final long CAT_ID_MAIL     = 2;
    public static final long CAT_ID_SOCIAL   = 3;
    public static final long CAT_ID_CARDS    = 4;
    public static final long CAT_ID_COMPUTERS  = 5;
    public static final long CAT_ID_DEVICE   = 6;
    public static final long CAT_ID_ENTRY    = 7;
    public static final long CAT_ID_WEBSITE  = 8;

    public static final int TYPE_MASTER = 0x5020;
    public static final int TYPE_QUICK  = 0x5022;

    public static final int REQUEST_CODE_ADD_TYPE = 0x6001;
    public static final int REQUEST_CODE_ADD_CATE = 0x6002;
    public static final int REQUEST_CODE_IMAGE     = 0x6003;
    public static final int REQUEST_CODE_CROP      = 0x6004;
}
