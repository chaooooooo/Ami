package chao.app.ami.classes;


import java.util.ArrayList;
import java.util.Stack;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

class FrameProcessor {

    private Stack<Frame> mFrameStack = new Stack<>();

    private ArrayList<ClassesManager.TopFrameChangedListener> mListeners = new ArrayList<>();

    public void pushInto(Object object) {
        Frame frame = new Frame(object);
        mFrameStack.push(frame);
        String path = "";
        for (Frame f: mFrameStack) {
            path = path + "/" + f.getName();
        }
        for (ClassesManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(frame, path);
        }
    }

    public Frame popOut() {
        Frame frame = mFrameStack.pop();
        String path = "";
        for (Frame f: mFrameStack) {
            path = path + "/" + f.getName();
        }
        for (ClassesManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(frame, path);
        }
        return frame;
    }

    public Frame peek() {
        return mFrameStack.peek();
    }

    public void clear() {
        mFrameStack.clear();
    }

    public static void addTopFrameChangeListener() {

    }

    public void addFrameChangeListener(ClassesManager.TopFrameChangedListener listener) {
        if (listener == null) {
            return;
        }
        mListeners.add(listener);
    }

    public void removeFrameChangeListener(ClassesManager.TopFrameChangedListener listener) {
        if (listener == null) {
            return;
        }
        mListeners.remove(listener);
    }
}
