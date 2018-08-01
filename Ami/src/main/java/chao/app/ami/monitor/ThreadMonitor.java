package chao.app.ami.monitor;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadPoolExecutor;


public class ThreadMonitor {

    private static final SourceInfo EMPTY_SOURCE_INFO = new SourceInfo();

    private static ThreadMonitor sInstance;

    private BufferedWriter mWriter;

    private SimpleDateFormat mTimestampFormat = new SimpleDateFormat("yyyyMMdd.HHmmss: ", Locale.CHINA);

    private final WeakHashMap<Thread, ThreadInfo> mCache = new WeakHashMap<>(100);

//    private WeakHashMap<ThreadPoolExecutor, ThreadPoolExecutorInfo> mThreadPoolCache = new WeakHashMap<>(50);

    private ThreadMonitor() {
    }

    public void init(Application app) {
        File dir = getThreadLogDir(app);

        if (!dir.exists() && !dir.mkdir()) {
            Log.e("luqin", "mkdir " + dir.getAbsolutePath() + " failed!");
            return;
        }
        Log.e("luqin", "tlog dir --> " + dir.getAbsolutePath());
        int pid = Process.myPid();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss", Locale.CHINA);
        String time = dateFormat.format(new Date());
        File logFile = new File(dir, "Thread-" + pid + "-" + time + ".log");
        try {
            if (!logFile.createNewFile()) {
                Log.e("luqin", "craete new file " + logFile.getAbsolutePath() + " failed!");
                return;
            }

            FileWriter fileWriter = new FileWriter(logFile);
            mWriter = new BufferedWriter(fileWriter);
            appendFile("pid:" + pid, false);
            appendFile("create timestamp: " + time, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static ThreadMonitor instance() {
        if (sInstance == null) {
            sInstance = new ThreadMonitor();
        }
        return sInstance;
    }

    private static File getThreadLogDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            if(externalCacheDir != null) {
                cachePath = context.getExternalCacheDir().getPath();
            }
            log("external cachePath : " + cachePath);
        }
        if (TextUtils.isEmpty(cachePath)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cachePath = context.getDataDir().getPath();
            }
        }
        log("final cachePath : " + cachePath);
        return new File(cachePath + File.separator + "tlog");
    }

    void appendFile(String log, boolean flush) {
        if (mWriter == null) {
            return;
        }
        try {
            log(log);
            mWriter.write(mTimestampFormat.format(new Date()));
            mWriter.write(log);
            mWriter.newLine();
            if (flush) {
                mWriter.flush();
            }
        } catch (Exception e) {
            try {
                mWriter.close();
            } catch (IOException e1) {
                // do nothing
            }
            e.printStackTrace();
        }
    }


    public void dumpThreadInfo() {

        Map map = Thread.getAllStackTraces();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Thread thread = (Thread) iterator.next();
            cacheThread(thread, null, null);
        }
        List<Thread> list = new ArrayList<>(mCache.keySet());
        Collections.sort(list, new Comparator<Thread>() {
            @Override
            public int compare(Thread o1, Thread o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });

        appendFile("dump timestamp: " + mTimestampFormat.format(new Date()), false);
        appendFile("all stack size: " + map.size(), false);
        appendFile("thread count: " + mCache.size(), false);
        appendFile("--------------- dump started! ---------------", false);
        for (Thread thread: list) {
            ThreadInfo threadInfo = mCache.get(thread);
            if (threadInfo == null) {
                threadInfo = new ThreadInfo(thread, "", new SourceInfo());
            }
            appendFile(threadInfo.toString(), true);
        }
    }

    public static void log(String log) {
        Log.e("luqin", log);
    }

    public void cacheThread(Thread thread, String poolName, SourceInfo sourceInfo) {
        if (sourceInfo == null) {
            sourceInfo = EMPTY_SOURCE_INFO;
        }
        synchronized (mCache) {
            if (mCache.containsKey(thread)) {
                ThreadInfo info = mCache.get(thread);
                if (info != null && !TextUtils.isEmpty(poolName)) {
                    info.setExecutorName(poolName);
                }
                if (info != null && sourceInfo != EMPTY_SOURCE_INFO) {
                    info.setSourceInfo(sourceInfo);
                }
                return;
            }
            mCache.put(thread, new ThreadInfo(thread, poolName, sourceInfo));
        }
//        if (sourceInfo.getFileName() != null && sourceInfo.getFileName().contains("YoukuHTTPD")) {
//            log(sourceInfo.getFileName() + ":" + sourceInfo.getLineNumber() + ":" + thread.getName());
//        }
    }

    public void cacheThreadPool(ThreadPoolExecutor threadPoolExecutor, String s) {
        SourceInfo sourceInfo = ThreadUtil.getSourceLocation(ThreadMonitor.class.getSimpleName());
        ThreadPoolExecutorInfo info = new ThreadPoolExecutorInfo(threadPoolExecutor, sourceInfo);
//        mThreadPoolCache.put(threadPoolExecutor, info);
        for (Thread thread: info.getThreads()) {
            cacheThread(thread, info.getClass().getName(), ThreadUtil.getSourceLocation());
        }
    }
}
