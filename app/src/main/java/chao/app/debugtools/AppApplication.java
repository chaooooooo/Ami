package chao.app.debugtools;

import android.annotation.SuppressLint;
import android.app.Application;
import chao.app.ami.Ami;
import java.io.File;

/**
 * @author chao.qin
 * @since 2017/7/24
 */

@SuppressLint("Registered")
public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        File path = getExternalCacheDir();
        String traceFile = path.getAbsolutePath() + "/" + System.currentTimeMillis() + "ami.trace";

//        Debug.startMethodTracing(traceFile, 30 * 1024 * 1024);
        Ami.init(this);
        Ami.setDrawerId(R.raw.drawer);
        Ami.setLifecycleLevel(Ami.LIFECYCLE_LEVEL_NONE);

//        Debug.stopMethodTracing();
    }


}
