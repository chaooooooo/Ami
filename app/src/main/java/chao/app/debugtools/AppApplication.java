package chao.app.debugtools;

import android.app.Application;

import chao.app.ami.AMi;

/**
 * @author chao.qin
 * @since 2017/7/24
 */

public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AMi.init(this, R.raw.drawer);

    }


}
