package chao.app.debugtools;

import android.app.Application;

import chao.app.ami.Ami;

/**
 * @author chao.qin
 * @since 2017/7/24
 */

public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Ami.init(this);
        Ami.setDrawerId(R.raw.drawer);
        Ami.setViewInterceptorEnabled(true);
        Ami.setLifecycleLevel(Ami.LIFECYCLE_LEVEL_CREATE);

//        MonitorManager.init(this);
    }


}
