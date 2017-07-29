package chao.app.ami;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

import chao.app.ami.launcher.drawer.DrawerManager;
import chao.app.ami.proxy.ProxyManager;

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
        if (mInstance != null) {
            return;
        }
        mInstance = new AMi(app);
        DrawerManager.init(app, drawerId);
        ProxyManager.init(app);
        try {
            ProxyManager.setProxy("192.168.10.10", 8080);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public static Application getApp() {
        return mApp;
    }
}
