package chao.app.ami.plugin.plugins.logcat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import chao.app.ami.Ami;
import chao.app.ami.logs.LogHelper;
import chao.app.ami.utils.command.Shell;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinchao
 * @since 2018/9/5
 */
public class LogcatManager {

    private static final String TAG = LogcatManager.class.getSimpleName();

    private final static int HANDLER_WHAT_REFRESH = 1;

    private static final int HANDLER_WHAT_HEART_BREAK = 2;

    private static final int HEART_BREAK_INTERVAL = 5000;

    private ArrayList<LogcatLine> logCaches = new ArrayList<>(300);

    private ArrayList<LogcatLine> pendingLogs = new ArrayList<>(50);

    private LogcatPlugin logcatPlugin;

    private Shell.Interactive mLogcat;

    private LogcatSettings settings;

    private String pid;

    private boolean searchMode = false;

    private SearchParam searchParam;


    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HANDLER_WHAT_REFRESH) {
                if (settings.isPause()) {
                    return true;
                }
                int count = pendingLogs.size();
                int cacheSize = settings.getCacheSize();
                if (logCaches.size() + count > cacheSize) {
                    List<LogcatLine> subList = new ArrayList<>(logCaches.subList(0, cacheSize - count));
                    logCaches.clear();
                    logCaches.addAll(pendingLogs);
                    logCaches.addAll(subList);
                } else {
                    logCaches.addAll(0, pendingLogs);
                }
                pendingLogs.clear();
                if (!searchMode) {
                    logcatPlugin.notifyDataSetChanged(logCaches);
                    return true;
                }
                logcatPlugin.notifyDataSetChanged(filter());
                return true;
            } else if (msg.what == HANDLER_WHAT_HEART_BREAK) {
                Ami.log("heart break test " + Math.random());
                notifyHeartBreak();
            }
            return false;
        }
    });

    private ArrayList<LogcatLine> filter() {
        ArrayList<LogcatLine> logs = new ArrayList<>();
        for(LogcatLine line: logCaches) {
            LogLevel paramLv = searchParam.getLevel();
            if (paramLv != null && !paramLv.equals(line.getLevel())) {
                continue;
            }
            String tid = searchParam.getTid();
            if (tid != null && !tid.equals(line.getTid())) {
                continue;
            }
            String tag = searchParam.getTag();
            if (tag != null && !tag.equals(line.getTag())) {
                continue;
            }
            logs.add(line);
        }
        return logs;
    }

    public void notifyRefreshUI() {
        cancelRefreshUI();
        Message message = mHandler.obtainMessage(HANDLER_WHAT_REFRESH);
        mHandler.sendMessageDelayed(message, 500);
    }

    public void cancelRefreshUI() {
        mHandler.removeMessages(HANDLER_WHAT_REFRESH);
    }

    public void notifyHeartBreak() {
        if (!settings.isHeart()) {
            return;
        }
        mHandler.removeMessages(HANDLER_WHAT_HEART_BREAK);
        Message message = mHandler.obtainMessage(HANDLER_WHAT_HEART_BREAK);
        mHandler.sendMessageDelayed(message, HEART_BREAK_INTERVAL);
    }

    public void cancelNotifyHeartBreak() {
        mHandler.removeMessages(HANDLER_WHAT_HEART_BREAK);
    }

    public LogcatManager(LogcatPlugin logcatPlugin, LogcatSettings logcatSettings) {
        this.logcatPlugin = logcatPlugin;
        this.settings = logcatSettings;
        this.pid = String.valueOf(Process.myPid());
    }

    public void startLogcat() {
        if (mLogcat != null && mLogcat.isRunning()){
            return;
        }
//        stopLogcat();
        mLogcat = new Shell.Builder()
            .addCommand("logcat -v threadtime")
            .setOnStdoutLineListener(new Shell.OnCommandLineListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode) {
                    LogHelper.d(TAG, "std.onCommandResult: " + commandCode  + ", " + exitCode);
                    //todo for testing
                    throw new RuntimeException("logcat stopped!!!");
                }

                @Override
                public void onLine(String line) {
                    appendLogcat(line);
                }
            })
            .setOnStderrLineListener(new Shell.OnCommandLineListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode) {
                    LogHelper.d(TAG, "err.onCommandResult: " + commandCode  + ", " + exitCode);
                    //todo for testing
                    throw new RuntimeException("logcat stopped!!!");
                }

                @Override
                public void onLine(String line) {
                    appendLogcat(line);
                }
            })
            .open();
    }

    //会卡死
    public void stopLogcat() {
        if (mLogcat != null) {
            mLogcat.kill();
        }
    }

    public void appendLogcat(String line) {
        LogcatLine logcatLine = new LogcatLine(line);
        if (!pid.equals(logcatLine.getPid())) {
            //过滤非本进程的log
            //比如上次运行的log
            return;
        }
        if (logcatLine.getLevel().gte(settings.getLevel())) {
            pendingLogs.add(0, logcatLine);
        }
        if (pendingLogs.size() > settings.getCacheSize()) {
            pendingLogs.remove(pendingLogs.size() - 1);
        }
        notifyRefreshUI();
    }

    public void clear() {
        logCaches.clear();
        pendingLogs.clear();
        logcatPlugin.notifyDataSetCleared();
    }

    public void startSearch(SearchParam searchParam) {
        searchMode = true;
        this.searchParam = searchParam;
        notifyRefreshUI();
    }

    public void endSearch() {
        searchMode = false;
        searchParam = null;
        notifyRefreshUI();
    }
}
