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
    public static final int TYPE_MASTERPWD  = 0x236; //Event with master password.
    public static final int TYPE_FINISHED = 0x257;
    public static final int TYPE_CANCELED = 0x258;
    public static final int TYPE_MASTER_OK = 0x259;  //Master passwd is correct
    public static final int TYPE_MASTER_NO = 0x260;  //Master passwd is wrong

    public static final int TYPE_MASTER_CHANGE = 0x261; //Change master password

    //Category Type
    //Only custom type category can be deleted.
    public static final int CAT_TYPE_BUILTIN = 0x100;
    public static final int CAT_TYPE_CUSTOM  = 0x101;

    //Builtin Category ID
    public static final long CAT_ID_PRIVATE  = -1;
    public static final long CAT_ID_RECENT  = 1;
    public static final long CAT_ID_MAIL     = 2;
    public static final long CAT_ID_SOCIAL   = 3;
    public static final long CAT_ID_CARDS    = 4;
    public static final long CAT_ID_COMPUTERS  = 5;
    public static final long CAT_ID_DEVICE   = 6;
    public static final long CAT_ID_ENTRY    = 7;
    public static final long CAT_ID_WEBSITE  = 8;

    public static final int TYPE_MASTER = 0x5020;//The account type in db
    public static final int TYPE_QUICK  = 0x5022;

    public static final int REQUEST_CODE_ADD_TYPE = 0x6001;
    public static final int REQUEST_CODE_ADD_CATE = 0x6002;
    public static final int REQUEST_CODE_IMAGE     = 0x6003;
    public static final int REQUEST_CODE_CROP      = 0x6004;
    public static final int REQUEST_CODE_GEN_PWD   = 0x6005;
    public static final int REQUEST_CODE_EDIT      = 0x6006;

    public static final String SERVICE_CMD_START = "start";
    public static final String SERVICE_CMD_PASTE_ACCT = "paste_acct";
    public static final String SERVICE_CMD_PASTE_PSWD = "paste_pswd";
    public static final String SERVICE_CMD_PASTE_ADDT = "paste_addt";
}
