package linchange.example.com.supergreattest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lin Change on 2017-02-16.
 */
//SharedPreferences工具类
public class ShareUtils {
    public static final String NAME = "config";

    /**
     * 存入字符串
     * @param mContext 上下文
     * @param key 关键字
     * @param value 传入的值
     */
    public static void putString(Context mContext, String key, String value) {
        getSP(mContext).edit().putString(key, value).commit();
    }

    /**
     * 获取字符串
     * @param mContext 上下文
     * @param key 关键字
     * @param defValue 默认返回值
     */
    public static String getString(Context mContext, String key, String defValue) {
        return getSP(mContext).getString(key, defValue);
    }

    /**
     * 存入整数
     * @param mContext 上下文
     * @param key 关键字
     * @param value 传入的值
     */
    public static void putInt(Context mContext, String key, int value) {
        getSP(mContext).edit().putInt(key, value).commit();
    }

    /**
     * 获取整数
     * @param mContext 上下文
     * @param key 关键字
     * @param defValue 默认返回值
     */
    public static int getInt(Context mContext, String key, int defValue) {
        return getSP(mContext).getInt(key, defValue);
    }

    /**
     * 存入布尔值
     * @param mContext 上下文
     * @param key 关键字
     * @param value 传入的值
     */
    public static void putBoolean(Context mContext, String key, boolean value) {
        getSP(mContext).edit().putBoolean(key, value).commit();
    }

    /**
     * 获取布尔值
     * @param mContext 上下文
     * @param key 关键字
     * @param defValue 默认返回值
     */
    public static boolean getBoolean(Context mContext, String key, boolean defValue) {
        return getSP(mContext).getBoolean(key, defValue);
    }

    /**
     * 删除一条记录
     * @param mContext 上下文
     * @param key 关键字
     */
    public static void deleteShare(Context mContext, String key) {
        getSP(mContext).edit().remove(key).commit();
    }

    /**
     * 删除全部记录
     * @param mContext 上下文
     */
    public static void deleteAll(Context mContext) {
        getSP(mContext).edit().clear().commit();
    }

    /**
     * 返回一个根据上下文生成的SharedPreferences
     * @param mContext 上下文
     * @return 根据上下文生成的SharedPreferences
     */
    private static SharedPreferences getSP(Context mContext) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }
}
