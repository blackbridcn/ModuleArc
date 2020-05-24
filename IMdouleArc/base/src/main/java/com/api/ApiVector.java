package com.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.constant.BaseContstant;

import org.net.http.rxretrofit.NetSDK;
import org.utils.LogUtils;
import org.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: yuzzha
 * Date: 1/13/2020 5:53 PM
 * Description:
 * Remark:
 */
public class ApiVector {

    private static class ApiVerctorHolder {
        private final static ApiVector Instance = new ApiVector();
    }

    public static ApiVector getInstance() {
        return ApiVerctorHolder.Instance;
    }

    private static String[] API = new String[]{
            "https://test.peake.com.cn",
            "https://app.peake.com.cn",
            "http://192.168.1.28:8080",
            "http://192.168.1.40:8088"};

    //0:  代表HTTP; 1:代表WebSocket
    private static int[] NET_TYPE = new int[]{0, 1};
    //进入开发者页面需要输入的数字秘钥
    public static String DEV_SINGAL = "190923";

    public void doInitBaseUrlTask(Context mContext) {
        NetSDK.setBaseUrl(API[getBaseApiIndex(mContext)]);
    }

    //重置Api
    public void doResetBaseUrlTask(Context mContext, int positon) {
        if (positon <= API.length) {
            putStringValue(mContext, BaseContstant.KEY_BASE_URL, API[positon]);
            putIntValue(mContext, BaseContstant.KEY_URL_TYPE, positon);
            LogUtils.i("doResetBaseUrlTask : " + API[positon]);
            NetSDK.setBaseUrl(API[positon]);
        } else throw new RuntimeException("Service Setting is IndexOutOfBoundsException ");
    }

    public int getBaseApiIndex(Context mContext) {
        return getIntValue(mContext, BaseContstant.KEY_URL_TYPE, 1);
    }

    public void doResetNetTypeTask(Context mContext, int index) {
        if (index <= NET_TYPE.length - 1) {
            putIntValue(mContext, BaseContstant.KEY_CONN_TYPE, NET_TYPE[index]);
            putIntValue(mContext, BaseContstant.KEY_CONN_TYPE_INDEX, index);
        } else throw new RuntimeException("ConnType Setting is IndexOutOfBoundsException ");
    }

    public int getNetTypeIndex(Context mContext) {
        return getIntValue(mContext, BaseContstant.KEY_CONN_TYPE_INDEX, 0);
    }


    /*------------------------------------------------------------------------------------------------*/

    public static void putStringValue(Context context, String key, String value) {
        if (!StringUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static void putIntValue(Context context, String key, int value) {
        if (!StringUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static void putBooleanValue(Context context, String key, boolean value) {
        if (!StringUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static String getStringValue(Context context, String key, String defValue) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getString(key, defValue);
        }
        return defValue;
    }

    public static int getIntValue(Context context, String key, int value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getInt(key, value);
        }
        return value;
    }


    public static boolean getBooleanValue(Context context, String key, boolean value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getBoolean(key, value);
        }
        return value;
    }

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = BaseContstant.SP_DEVELOPER_FILE_NAME;
    private static SharedPreferences sp;

    public static SharedPreferences getSP(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences.Editor editor = getSP(context).edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = getSP(context);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        if (StringUtils.isNotEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.remove(key);
            SharedPreferencesCompat.apply(editor);
        }
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        private static Method findApplyMethod() {
            try {
                Class<SharedPreferences.Editor> clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                Log.e("TAG", e.toString());
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                Log.e("TAG", e.toString());
            }
            editor.commit();
        }
    }

}
