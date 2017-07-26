package chao.app.debugtools;

import android.app.Application;

import chao.app.debug.launcher.drawer.DrawerManager;

/**
 * @author chao.qin
 * @since 2017/7/24
 */

public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DrawerManager.init(this,R.raw.drawer);
    }


}
