package chao.app.ami.monitor;

import android.text.TextUtils;

import java.lang.ref.WeakReference;


public class ThreadInfo {

    private WeakReference<Thread> mThread;
    private SourceInfo mSourceInfo;
    private String mExecutorName;

    public ThreadInfo(Thread thread, String poolName, SourceInfo sourceInfo) {
        mThread = new WeakReference<>(thread);
        mExecutorName = poolName;
        mSourceInfo = sourceInfo;
    }

    public Thread getThread() {
        return mThread.get();
    }

    @Override
    public String toString() {
        if (mThread.get() == null) {
            return "<recycled>";
        }
        Thread thread = mThread.get();
        ThreadGroup group = thread.getThreadGroup();
        String owner = mSourceInfo.getFileName() + ":" + mSourceInfo.getLineNumber() + "-";
        if (!TextUtils.isEmpty(mExecutorName)) {
            owner += mExecutorName + "-";
        }
        String buffer;
        if (group != null) {
            buffer = owner + thread.getClass().getSimpleName() + "[" + thread.getName() + "," + thread.getId() + "," +
                    group.getName() + ", alive:" + thread.isAlive() + ", state:" + thread.getState() + "]";
        } else {
            buffer = owner + thread.getClass().getSimpleName() + "[" + thread.getName() + "," + thread.getId() + "," +
                    ", alive:" + thread.isAlive() + ", state:" + thread.getState() + "]";
        }
        buffer += "\r\n";
        for (StackTraceElement stack: mSourceInfo.getStackTrackElements()) {
            buffer += "\t";
            buffer += stack.toString();
            buffer += "\r\n";
        }
        buffer += "\r\n";
        for (StackTraceElement stack: mThread.get().getStackTrace()) {
            buffer += "\t";
            buffer += stack.toString();
            buffer += "\r\n";
        }
        return buffer;
    }

    public void setSourceInfo(SourceInfo sourceInfo) {
        mSourceInfo = sourceInfo;
    }

    public String getExecutorName() {
        return mExecutorName;
    }

    public void setExecutorName(String name) {
        mExecutorName = name;
    }

    @Override
    public int hashCode() {
        if (mThread == null) {
            return -1;
        }
        return (int) mThread.get().getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ThreadInfo)) {
            return false;
        }
        return hashCode() == obj.hashCode();
    }

}
