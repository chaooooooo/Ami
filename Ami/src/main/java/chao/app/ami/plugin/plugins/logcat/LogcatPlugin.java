package chao.app.ami.plugin.plugins.logcat;

import androidx.fragment.app.Fragment;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;
import java.util.ArrayList;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class LogcatPlugin extends AmiPlugin {

    private LogcatManager logcatManager;

    private LogcatSettings logcatSettings;

    public LogcatPlugin() {
        logcatSettings = new LogcatSettings();
        logcatManager = new LogcatManager(this, logcatSettings);
    }

    @Override
    public Fragment createFragment() {
        return new LogcatFragment();
    }

    @Override
    public LogcatPane createComponent() {
//        return new LogcatComponent(this);
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
        super.onCreate();
        logcatManager.startLogcat();
    }

    @Override
    public AmiSettings createSettings() {
        return logcatSettings;
    }

    @Override
    public void onBindView(AmiContentView contentView) {

    }

    public void notifyDataSetChanged(ArrayList<LogcatLine> logCaches) {
        LogcatFragment logcatFragment = getFragment();
        if (logcatFragment != null) {
            logcatFragment.notifyDataSetChanged(logCaches);
        }
    }

    public void notifyDataSetCleared() {
        LogcatFragment logcatFragment = getFragment();
        if (logcatFragment != null) {
            logcatFragment.notifyDataSetCleared();
        }
    }

    @Override
    public LogcatFragment getFragment() {
        return (LogcatFragment) super.getFragment();
    }
}
