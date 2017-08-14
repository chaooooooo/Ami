package chao.app.ami.frames;


import android.app.Activity;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

class FrameProcessor {

    private Stack<IFrame> mFrameStack = new Stack<>();

    private ArrayList<FrameManager.TopFrameChangedListener> mListeners = new ArrayList<>();

    public void pushActivity(Activity activity) {
        clear();
        BaseFrame baseFrame = new BaseFrame(activity);
        mFrameStack.push(baseFrame);
        for (FrameManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(baseFrame, "/" + baseFrame.getName());
        }
    }

    public void pushInto(Object object) {
        ObjectFrame frame = new ObjectFrame(object);
        mFrameStack.push(frame);
        String path = "";
        for (IFrame f: mFrameStack) {
            path = path + "/" + f.getName();
        }
        for (FrameManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(frame, path);
        }
    }

    public IFrame popOut() {
        if (mFrameStack.size() == 1) {
            return null;
        }
        IFrame frame = mFrameStack.pop();
        String path = "";
        for (IFrame f: mFrameStack) {
            path = path + "/" + f.getName();
        }
        for (FrameManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(frame, path);
        }
        return frame;
    }

    public IFrame peek() {
        return mFrameStack.peek();
    }

    public void clear() {
        mFrameStack.clear();
    }

    public static void addTopFrameChangeListener() {

    }

    public void addFrameChangeListener(FrameManager.TopFrameChangedListener listener) {
        if (listener == null) {
            return;
        }
        mListeners.add(listener);
    }

    public void removeFrameChangeListener(FrameManager.TopFrameChangedListener listener) {
        if (listener == null) {
            return;
        }
        mListeners.remove(listener);
    }
}
