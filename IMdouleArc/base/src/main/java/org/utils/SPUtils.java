package org.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cache.AppCacheManager;
import com.constant.BaseContstant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * yzzhang
 * <p>
 * SharedPreferences 工具类
 * <p>
 * 注意:为了兼容peake之前版本，所以Sp中存储的String类型值的Key需要以下处理[只有存储String时才需要特殊处理];
 * <p>
 * 对基本Key字段进行拼接,方法：原始Key+virtual_id  ;
 * <p>
 * 特别注意：以下4 种Key 不需要拼接
 * BaseContstant.KEY_LOGIN_ACCOUNT = "loginAccount";
 * BaseContstant.KEY_LOGIN_PASSWORD = "loginPassword";
 * BaseContstant.KEY_CARD_NUM = "card_num";
 * BaseContstant.KEY_LANGUAGE = "language";
 * </p>
 */
public class SPUtils {
    private static final String TAG = "SPUtils";
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = BaseContstant.SP_FILE_NAME;
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
            editor.putString(getPeakeKey(key), (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(getPeakeKey(key), object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = getSP(context);
        if (defaultObject instanceof String) {
            return sp.getString(getPeakeKey(key), (String) defaultObject);
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
     * 移除某个key值已经对应的值
     */
    public static void removeStringValue(Context context, String key) {
        remove(context, getPeakeKey(key));
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        return getSP(context).contains(key);
    }

    public static boolean containsString(Context context, String key) {
        return contains(context, getPeakeKey(key));
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        return getSP(context).getAll();
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
                Log.e(TAG, e.toString());
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
                Log.e(TAG, e.toString());
            }
            editor.commit();
        }
    }

    public static long getLongValue(Context context, String key, long value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getLong(key, value);
        }
        return 0;
    }

    public static long getLongValueByDefault(Context context, String key) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getLong(key, 0);
        }
        return 0;
    }

    public static String getStringValueByDefault(Context context, String key, String Value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getString(getPeakeKey(key), Value);
        }
        return null;
    }

    public static String getStringValue(Context context, String key) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getString(getPeakeKey(key), null);
        }
        return null;
    }

    public static int getIntValue(Context context, String key, int value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getInt(key, value);
        }
        return 0;
    }

    public static int getIntValueByDefault(Context context, String key) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getInt(key, 0);
        }
        return 0;
    }

    public static boolean getBooleanValue(Context context, String key, boolean value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getBoolean(key, value);
        }
        return false;
    }

    public static boolean getBooleanValueByDefault(Context context, String key) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getBoolean(key, false);
        }
        return false;
    }

    public static float getFloatValue(Context context, String key, float value) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getFloat(key, value);
        }
        return 0;
    }

    public static float getFloatValueByDefault(Context context, String key) {
        if (!StringUtils.isEmpty(key)) {
            return getSP(context).getFloat(key, 0);
        }
        return 0;
    }

    /**
     * 兼容peake之前版本中String类型Sp中 特殊 Key定义
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putStringValue(Context context, String key, String value) {
        if (!StringUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putString(getPeakeKey(key), value);
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

    public static void putLongValue(Context context, String key, long value) {
        if (!StringUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static void putFloatValue(Context context, String key, Float value) {
        if (!StringUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    /**
     * peake 拼接String类型的Key
     * <p>
     * 拼接方法：原始Key+virtual_id [只有存储String时才需要特殊处理] ;
     * 注意：以下4 种Key 不需要拼接
     * BaseContstant.KEY_LOGIN_ACCOUNT = "loginAccount";
     * BaseContstant.KEY_LOGIN_PASSWORD = "loginPassword";
     * BaseContstant.KEY_CARD_NUM = "card_num";
     * BaseContstant.KEY_LANGUAGE = "language";
     * BaseContstant.KEY_SECREEN_LOCK ="screen_secret";
     * </p>
     *
     * @param key
     * @return
     */
    private static String getPeakeKey(String key) {
        if (!StringUtils.isEmpty(key)) {
            if (!key.equals(BaseContstant.KEY_LANGUAGE)
                    && !key.equals(BaseContstant.KEY_LOGIN_PASSWORD)
                    && !key.equals(BaseContstant.KEY_LOGIN_ACCOUNT)
                    && !key.equals(BaseContstant.KEY_CARD_NUM)
                    && !key.equals(BaseContstant.KEY_BASE_URL)
                    && !key.equals(BaseContstant.KEY_SECREEN_LOCK)) {
                StringBuilder builder = new StringBuilder(2);
                builder.append(key).append(AppCacheManager.INSTANCE.getVirtual_id());
                return builder.toString();
            } else return key;
        } else return null;
    }

}
