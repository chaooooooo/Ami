package chao.app.ami.frames;

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

public class FrameManager extends ActivitiesLifeCycleAdapter {

    private static FrameManager sFrameManager;


    private FrameProcessor mFrameProcessor = new FrameProcessor();

    private FrameManager() {
    }

    public static void init() {
        if (sFrameManager != null) {
            return;
        }
        sFrameManager = new FrameManager();
        Ami.getApp().registerActivityLifecycleCallbacks(sFrameManager);
    }

    public static FrameManager getInstance() {
        if (sFrameManager == null) {
            throw new AmiException("ClassesManager should have initialization.");
        }
        return sFrameManager;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mFrameProcessor.clear();
        mFrameProcessor.pushActivity(activity);
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

    public void pushInto(SearchFrame searchFrame) {
        mFrameProcessor.pushInto(searchFrame);
    }

    public FrameImpl peek() {
        return mFrameProcessor.peek();
    }

    /**
     * 获取stack路径
     *
     */
    public String getPath() {
        return mFrameProcessor.buildPath();
    }


    public interface TopFrameChangedListener{
        void onTopFrameChanged(IFrame frame, String path);
    }
}
