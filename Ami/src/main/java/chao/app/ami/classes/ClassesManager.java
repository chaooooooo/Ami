package chao.app.ami.classes;

import android.app.Activity;

import chao.app.ami.ActivitiesLifeCycleAdapter;
import chao.app.ami.Ami;
import chao.app.ami.AmiException;
import chao.app.ami.launcher.drawer.DrawerManager;

/**
 * @author chao.qin
 * @since 2017/8/2
 *
 */

public class ClassesManager extends ActivitiesLifeCycleAdapter {

    private static ClassesManager sClassesManager;


    private FrameProcessor mFrameProcessor = new FrameProcessor();

    private ClassesManager() {
    }

    public static void init() {
        if (sClassesManager != null) {
            return;
        }
        sClassesManager = new ClassesManager();
        Ami.getApp().registerActivityLifecycleCallbacks(sClassesManager);
    }

    public static ClassesManager getInstance() {
        if (sClassesManager == null) {
            throw new AmiException("ClassesManager should have initialization.");
        }
        return sClassesManager;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mFrameProcessor.clear();
        mFrameProcessor.pushInto(activity);
        notifyFrameChanged();
    }

    FrameProcessor getFrameProcessor() {
        return mFrameProcessor;
    }

    public void notifyFrameChanged() {
        DrawerManager.get().notifyFrameChanged();
    }

    public void addFrameChangeListener(TopFrameChangedListener listener) {
        mFrameProcessor.addFrameChangeListener(listener);
    }

    public void removeFrameChangeListener(TopFrameChangedListener listener) {
        mFrameProcessor.removeFrameChangeListener(listener);
    }


    public interface TopFrameChangedListener{
        void onTopFrameChanged(Frame frame, String path);
    }
}
