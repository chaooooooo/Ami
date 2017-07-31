package chao.app.ami;

import android.app.Application;
import android.util.Log;

import chao.app.ami.launcher.drawer.DrawerManager;
import chao.app.ami.proxy.ProxyManager;
import chao.app.ami.text.TextManager;
import chao.app.ami.utils.Util;

/**
 * @author chao.qin
 * @since 2017/7/27
 */

public class AMi {

    private static final String TAG = "AMI";
    private static Application mApp;
    private static AMi mInstance;

    public static final int LIFECYCLE_LEVEL_FULL = 3;
    public static final int LIFECYCLE_LEVEL_SIMPLE = 2;
    public static final int LIFECYCLE_LEVEL_CREATE = 1;
    public static final int LIFECYCLE_LEVEL_NONE = 0;

    private static int mLifecycle = LIFECYCLE_LEVEL_NONE;

    private AMi(Application app) {
        mApp = app;
    }

    public static void init(Application app, int drawerId) {
        if (!Util.isHostAppDebugMode(app)) {
            return;
        }
        if (mInstance != null) {
            return;
        }
        mInstance = new AMi(app);
        DrawerManager.init(app, drawerId);
        ProxyManager.init(app);
        TextManager.init();
    }


    public static Application getApp() {
        return mApp;
    }

    public static void setLifecycleLevel(int level) {
        mLifecycle = level;
    }

    public static void log(String log) {
        log(TAG, log);
    }

    public static void log(String tag, String log) {
        Log.d(tag, log);
    }

    public static void lifecycle(String tag, String log, int level) {
        if (level < mLifecycle) {
            return;
        }
        log(tag, log);
    }


}
