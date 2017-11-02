package chao.app.ami.monitor;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by qinchao on 2017/9/6.
 */

public class ThreadPoolExecutorInfo {

    private ThreadPoolExecutor mThreadPool;
    private String mOwner;

    private SourceInfo mSourceInfo;

    private static Field ThreadPoolExecutor_workers;
    private static Field Worker_thread;

    static {
        try {
            ThreadPoolExecutor_workers = ThreadPoolExecutor.class.getDeclaredField("workers");
            ThreadPoolExecutor_workers.setAccessible(true);

            Class clazzWorker = Class.forName(ThreadPoolExecutor.class.getName() + "$Worker");
            Worker_thread = clazzWorker.getDeclaredField("thread");
            Worker_thread.setAccessible(true);
        } catch (Throwable e) {
            throw new RuntimeException("thread pool workers not found!", e);
        }

    }

    public ThreadPoolExecutorInfo(ThreadPoolExecutor threadPool, SourceInfo info) {
        mThreadPool = threadPool;
        mSourceInfo = info;
        mOwner = info.getFileName() + ":" + info.getLineNumber() + "-";
    }

    public Thread[] getThreads() {
        Thread[] threads = null;
        try {
            HashSet workers = (HashSet) ThreadPoolExecutor_workers.get(mThreadPool);
            int threadCount = workers.size();
            threads = new Thread[threadCount];
            int index = 0;
            for (Object worker: workers) {
                Thread thread = (Thread) Worker_thread.get(worker);
                threads[index++] = thread;
            }
        } catch (IllegalAccessException e) {
            threads = new Thread[0];
        }
        return threads;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("TPE-" + mOwner + "{");
        try {
            HashSet workers = (HashSet) ThreadPoolExecutor_workers.get(mThreadPool);

            for (Object worker: workers) {
                Thread thread = (Thread) Worker_thread.get(worker);
                stringBuilder.append(ThreadUtil.threadString(thread)).append(", ");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void saveToFile() {

    }
}
