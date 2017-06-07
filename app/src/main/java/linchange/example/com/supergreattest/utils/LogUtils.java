package linchange.example.com.supergreattest.utils;

import android.util.Log;

/**
 * Created by Lin Change on 2017-02-16.
 */
//Log工具类
public class LogUtils {
    //Log开关
    public static final boolean DEBUG = true;
    //TAG
//    public static final String TAG = "SuperGreatTest";

    /**
     * debug方法
     * @param text 需要打印的文字信息
     */
    public static void d(String tag, String text) {
        if (DEBUG) {
            Log.d(tag, text);
        }
    }

    /**
     * info方法
     * @param text 需要打印的文字信息
     */
    public static void i(String tag, String text) {
        if (DEBUG) {
            Log.d(tag, text);
        }
    }

    /**
     * wrong方法
     * @param text 需要打印的文字信息
     */
    public static void w(String tag, String text) {
        if (DEBUG) {
            Log.d(tag, text);
        }
    }

    /**
     * error方法
     * @param text 需要打印的文字信息
     */
    public static void e(String tag, String text) {
        if (DEBUG) {
            Log.d(tag, text);
        }
    }
}
