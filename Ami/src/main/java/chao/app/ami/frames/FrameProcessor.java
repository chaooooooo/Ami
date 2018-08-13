package chao.app.ami.frames;


import android.app.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

class FrameProcessor {

    private Stack<FrameImpl> mFrameStack = new Stack<>();

    private ArrayList<FrameManager.TopFrameChangedListener> mListeners = new ArrayList<>();

    public void pushActivity(Activity activity) {
        clear();
        FirstFrame firstFrame = new FirstFrame(activity);
        mFrameStack.push(firstFrame);
        for (FrameManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(firstFrame, "/");
        }
    }

    private boolean isArray(Object object) {
        return object != null && object.getClass().isArray();
    }

    private boolean isCollectionObject(Object obj) {
        return obj != null && obj instanceof Collection;
    }

    private boolean isMap(Object obj) {
        return obj != null && obj instanceof Map;
    }

    public boolean pushInto(IFrame.Entry entry) {
        Object object = entry.getValue();
        if (object == null) {
            return false;
        }
        if (object instanceof Number || object instanceof Boolean || object instanceof String) {
            return false;
        }
        if (object instanceof Class) {
            return false;
        }
        FrameImpl frame;
        if (isArray(object)) {
            frame = new ArrayFrame(String.valueOf(entry.title), object);
        } else if (isCollectionObject(object)) {
            Collection collection = (Collection) object;
            Object[] array = collection.toArray();
            frame = new ArrayFrame(String.valueOf(entry.title), array);
        } else if (isMap(object)) {
            frame = new MapFrame(String.valueOf(entry.title), (Map)object);
        } else {
            frame = new ObjectFrame(object);
        }
        pushInto(frame);
        return true;
    }

    public void pushInto(FrameImpl frame) {
        mFrameStack.push(frame);
        String path = buildPath();
        for (FrameManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(frame, path);
        }
    }

    public String buildPath() {
        StringBuilder path = new StringBuilder();
        for (IFrame f: mFrameStack) {
            if (f instanceof FirstFrame) {
                continue;
            }
            path.append("/").append(f.getName());
        }
        return path.toString();
    }

    public IFrame popOut() {
        if (mFrameStack.size() == 1) {
            return null;
        }
        IFrame frame = mFrameStack.pop();
        String path = "";
        for (IFrame f: mFrameStack) {
            if (f instanceof FirstFrame) {
                continue;
            }
            path = path + "/" + f.getName();
        }
        for (FrameManager.TopFrameChangedListener listener: mListeners) {
            listener.onTopFrameChanged(frame, path);
        }
        return frame;
    }

    public FrameImpl peek() {
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
