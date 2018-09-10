package chao.app.ami.plugin.plugins.logcat;

import android.support.v4.app.Fragment;
import chao.app.ami.plugin.AmiBasePlugin;
import chao.app.ami.plugin.AmiSettings;
import java.util.ArrayList;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class LogcatPlugin extends AmiBasePlugin {

    private LogcatManager logcatManager;

    private LogcatFragment logcatFragment;

    private LogcatSettings logcatSettings;

    public LogcatPlugin() {
        logcatSettings = new LogcatSettings();
        logcatManager = new LogcatManager(this, logcatSettings);
    }

    public LogcatManager getLogcatManager() {
        return logcatManager;
    }

    @Override
    protected Fragment createFragment() {
        return new LogcatFragment();
    }

    @Override
    public CharSequence getTitle() {
        return "logcat";
    }

    @Override
    public void onCreate() {
        logcatManager.startLogcat();
        logcatFragment = (LogcatFragment) getFragment();
    }

    @Override
    public AmiSettings getSettings() {
        return logcatSettings;
    }

    public void notifyDataSetChanged(ArrayList<LogcatLine> logCaches, int pos, int length) {
        if (logcatFragment != null) {
            logcatFragment.notifyDataSetChanged(logCaches, pos, length);
        }
    }

    public void notifyDataSetCleared() {
        if (logcatFragment != null) {
            logcatFragment.notifyDataSetCleared();
        }
    }

}
