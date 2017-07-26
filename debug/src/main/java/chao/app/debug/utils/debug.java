package chao.app.debug.utils;

import android.util.Log;

/**
 * @author chao.qin
 * @since 2017/7/24
 */

public class debug {
    private static final String TAG = "debug";
    public static void log(String log) {
        Log.d(TAG, log);
    }
}
