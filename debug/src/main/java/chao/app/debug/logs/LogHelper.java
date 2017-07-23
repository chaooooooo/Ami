package chao.app.debug.logs;

import android.util.Log;

/**
 * @author chao.qin
 * @since 2017/3/24
 */

public class LogHelper {

    public static int e(String tag, String log) {
        return Log.e(tag, log);
    }

    public static int v(String tag, String log) {
        return Log.v(tag, log);
    }

    public static int d(String tag, String log) {
        return Log.d(tag, log);
    }

    public static int i(String tag, String log) {
        return Log.i(tag, log);
    }

    public static int w(String tag, String log) {
        return Log.w(tag, log);
    }
}
