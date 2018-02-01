package chao.app.debugtools;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import chao.app.ami.Ami;

/**
 * @author chao.qin
 * @since 2017/7/24
 */

public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
//        Ami.init(this, R.raw.drawer);
        Ami.init(this);
        Ami.setDrawerId(R.raw.drawer);
        Ami.setViewInterceptorEnabled(false);
        Ami.setLifecycleLevel(Ami.LIFECYCLE_LEVEL_CREATE);

    }


}
