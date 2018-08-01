package chao.app.ami.monitor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by qinchao on 2017/9/11.
 */

public class MonitorManager {

    @SuppressLint("StaticFieldLeak")
    private static Application mApp;
    private final ArrayList<AbsMonitor> mMonitors = new ArrayList<>();

    public static Application getApp() {
        return mApp;
    }

    private static class Singleton {
        @SuppressLint("StaticFieldLeak")
        private static final MonitorManager INSTANCE = new MonitorManager();
    }

    public static MonitorManager getInstance() {
        return Singleton.INSTANCE;
    }

    private MonitorManager() {
        Log.e("luqin", "new MonitorManager.");
    }

    public static void init(Application app) {
        Log.e("luqin", "Monitor Manager init : " + app);
        mApp = app;
        getThreadMonitor().init(app);

        getInstance().registerMonitor(new DumpMonitor(mApp));
        getInstance().registerMonitor(new GCMonitor(mApp));
        getInstance().registerMonitor(new ProcessSignalMonitor(mApp));

        ExceptionHandler exceptionHandler = new ExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
    }

    public static ThreadMonitor getThreadMonitor() {
        return ThreadMonitor.instance();
    }

    private void registerMonitor(AbsMonitor server) {
        mMonitors.add(server);
    }

    private void unregisterServer(AbsMonitor server) {
        mMonitors.remove(server);
    }

    public void clear() {
        mMonitors.clear();
    }

    public ArrayList<AbsMonitor> getMonitors() {
        return mMonitors;
    }

}
