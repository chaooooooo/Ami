package chao.app.ami.plugin.plugins.logcat;

import android.support.v4.app.Fragment;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.AmiGeneralComponent;
import java.util.ArrayList;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class LogcatPlugin extends AmiPlugin {

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
    public Fragment createFragment() {
        logcatFragment = new LogcatFragment();
        return logcatFragment;
    }

    @Override
    public AmiGeneralComponent getComponent() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return "logcat";
    }

    @Override
    public Object getManager() {
        return logcatManager;
    }

    @Override
    public void onCreate() {
        logcatManager.startLogcat();
    }

    @Override
    public void onBindView(AmiContentView contentView) {

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
