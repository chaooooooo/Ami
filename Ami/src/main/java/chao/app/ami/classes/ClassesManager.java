package chao.app.ami.classes;

import android.app.Activity;

import chao.app.ami.ActivitiesLifeCycleAdapter;
import chao.app.ami.Ami;

/**
 * @author chao.qin
 * @since 2017/8/2
 *
 */

public class ClassesManager extends ActivitiesLifeCycleAdapter {

    private static ClassesManager sClassesManager;

    private ClassesManager() {
    }

    public static void init() {
        if (sClassesManager != null) {
            return;
        }
        sClassesManager = new ClassesManager();
        Ami.getApp().registerActivityLifecycleCallbacks(sClassesManager);
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        FrameProcessor.process(activity);
    }
}
