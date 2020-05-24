package com.constant;

import android.os.Environment;

/**
 * Author: yuzzha
 * Date: 2019-08-20 16:45
 * Description: ${DESCRIPTION}
 * Remark:
 */
public interface BaseContstant {
    /*#################################### NFC #########################################*/
    //披克NFC AID
    String PEAKE_NFC_HCE_CARD_AID = "F0000002110001010001";
    //Android NFC   Card Emulation 命令头；
    String NFC_HCE_SELECT_APDU_HEADER = "00A40400";
    /*#################################### NFC #########################################*/
    String BuglyAppId = "7b7e0d83a3";
    String BuglyAppKey = "72274cc7-825e-45a5-8d3c-95247cf06ca2";

    //PEAKE App中的SharedPreferences File文件名
    String SP_FILE_NAME = "pikeCloud";
    //PEAKE App中科大讯飞的语音AppId  iflytek
    String SpeechAppId = "5941f339 ";

    //轮播图片的比例
    int BannerRatio = 2;

    //Cache 缓存中常量设置
    String CACHR_DISK_UNIQUE_NAME = "peake_cache";
    int CACHR_DISK_MAX_COUNT = 1;
    int CACHR_DISK_MAX_SIZE = 50 * 1024 * 1024;

    //X5浏览器中缓存路径
    String KEY_APP_CACAHE_PATH = "PeakeCache";
    String KEY_APP_DATA_PATH = "PeakeData";
    String KEY_APP_GEOLOACTION_PATH = "PeakeGeo";
    /**
     * /**
     * 分页参数
     */
    int DEF_PAGE_SIZE = 10;
    int DEF_PAGE_NO = 0;

    //Android 7.0 授权File path
    String AUTHOR_FILE = "com.poobo.peakecloud.fileprovider";

    //访客拍照裁剪图片存储图片名
    String VISIT_PIC = "peake_visit_small.png";
    //apk中照片的Stroage
    String APK_PIC_DIR = Environment.DIRECTORY_PICTURES;

    /*#################################### Module DB NAME & VERSION #########################################*/
    String SQLITE_FEE_DB_NAME = "peake_fee.db";
    int SQLITE_FEE_DB_VERSION = 1;
    String SQLITE_LOGIN_DB_NAME = "peake_login.db";
    int SQLITE_LOGIN_DB_VERSION = 1;
    String SQLITE_CARD_DB_NAME = "peake_card.db";
    int SQLITE_CARD_DB_VERSION = 1;

    /*#################################### 页面跳转 传值KEY  VALUE#########################################*/

    String EXTRA_KEY_TYPE = "extra_type";
    String EXTRA_KEY_TITLE = "extra_title";
    String EXTRA_KEY_URL = "extra_url";
    String EXTRA_KEY_COMM = "extra_comm";


    /*#################################### BroadcastReceiver Action #########################################*/
    String ACTION_PUSH_PASS_CERTIFICATE_USED = "com.peake.push.pass.certificate.used.action";

    /*#################################### BroadcastReceiver Action #########################################*/

    /*#################################### SharedPreferences KEY #########################################*/

    String KEY_AUTO_LOGIN = "autologin";

    String KEY_LOGIN_ACCOUNT = "loginAccount";
    String KEY_LOGIN_PASSWORD = "loginPassword";
    String KEY_CARD_NUM = "card_num";
    String KEY_LANGUAGE = "language";
    //登录时间
    String KEY_LOGIN_DATE = "login_date";
    //门禁密码
    String KEY_DOOR_PSW = "key_door_psw";
    // 用于APK升级时记录用户取消升级版本号
    String KEY_APK_VER = "APK_NEW_VERSION_IGNOR_";

    String KEY_APK_VERSION = "version";


    String KEY_OPETATOR_CHANGED = "opetatorChanged";
    String KEY_OPERATE_ID = "operateId";
    String KEY_OPERATE_NAME = "operateName";
    String KEY_SYS_ID = "sys_id";
    String KEY_RECORD_ID = "record_id";

    String SHAKE_DISTANCE_SETTING = "shake_distance_setting";
    String KEY_COMPANY_NAME = "company_name";

    //App 内屏幕锁
    String KEY_SECREEN_LOCK = "screen_secret";

    /*#################################### Developer KEY #########################################*/
    String SP_DEVELOPER_FILE_NAME = "pikeCloud";
    //测试模式
    String KEY_TEST_MODULE = "KEY_PEAKE_TEST_MODLE";
    //默认显示服务器
    String KEY_URL_TYPE = "KEY_URL_TYPE";

    String KEY_TEST_CARD_MODEL = "KEY_TEST_CARD_MODEL";
    String KEY_TEST_CARD_ID = "KEY_TEST_CARD_ID";

    //BaseUrl 用于修改
    String KEY_BASE_URL = "CURRENT_BASE_URL";
    //
    String KEY_CONN_TYPE = "KEY_CONN_TYPE";
    String KEY_CONN_TYPE_INDEX = "KEY_CONN_TYPE_INDEX";
    /*#################################### Developer KEY #########################################*/


}
