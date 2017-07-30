package chao.app.ami;

import android.app.Application;

import chao.app.ami.launcher.drawer.DrawerManager;
import chao.app.ami.proxy.ProxyManager;
import chao.app.ami.text.TextManager;
import chao.app.ami.utils.Util;

/**
 * @author chao.qin
 * @since 2017/7/27
 */

public class AMi {

    private static Application mApp;
    private static AMi mInstance;

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
}
