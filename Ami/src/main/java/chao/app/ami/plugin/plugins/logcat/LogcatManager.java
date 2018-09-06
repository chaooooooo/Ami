package chao.app.ami.plugin.plugins.logcat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import chao.app.ami.Ami;
import chao.app.ami.utils.command.Shell;
import chao.app.ami.utils.command.StreamGobbler;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinchao
 * @since 2018/9/5
 */
public class LogcatManager {

    private final static int HANDLER_WHAT_REFRESH = 1;

    private static final int HANDLER_WHAT_HEART_BREAK = 2;

    private ArrayList<LogcatLine> logCaches = new ArrayList<>(300);

    private ArrayList<LogcatLine> pendingLogs = new ArrayList<>(50);

    private LogcatPlugin logcatPlugin;

    private Shell.Interactive mLogcat;

    private LogcatSettings settings;


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
                logcatPlugin.notifyDataSetChanged(logCaches, 0, count);
                return true;
            } else if (msg.what == HANDLER_WHAT_HEART_BREAK) {
                Ami.log("heart break test " + Math.random());
                notifyHeartBreak();
            }
            return false;
        }
    });

    public void notifyRefreshUI() {
        cancelRefreshUI();
        Message message = mHandler.obtainMessage(HANDLER_WHAT_REFRESH);
        mHandler.sendMessageDelayed(message, 500);
    }

    public void cancelRefreshUI() {
        mHandler.removeMessages(HANDLER_WHAT_REFRESH);
    }

    private void notifyHeartBreak() {
        int heart = settings.getHeart();
        if (heart <= 0) {
            return;
        }
        mHandler.removeMessages(HANDLER_WHAT_HEART_BREAK);
        Message message = mHandler.obtainMessage(HANDLER_WHAT_HEART_BREAK);
        mHandler.sendMessageDelayed(message, heart);
    }

    public LogcatManager(LogcatPlugin logcatPlugin, LogcatSettings logcatSettings) {
        this.logcatPlugin = logcatPlugin;
        this.settings = logcatSettings;
        notifyHeartBreak();
    }

    public void startLogcat() {
        if (mLogcat != null && mLogcat.isRunning()){
            return;
        }
        mLogcat = new Shell.Builder()
            .addCommand("locat -c | logcat -v threadtime")
            .setOnStdoutLineListener(new StreamGobbler.OnLineListener() {
                @Override
                public void onLine(String line) {
                    appendLogcat(line);
                }
            })
            .setOnStderrLineListener(new StreamGobbler.OnLineListener() {
                @Override
                public void onLine(String line) {
                    appendLogcat(line);
                }
            })
            .open();
    }

    public void stopLogcat() {
        if (mLogcat != null) {
            mLogcat.kill();
        }
    }

    public void appendLogcat(String line) {
        LogcatLine logcatLine = new LogcatLine(line);
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
}
