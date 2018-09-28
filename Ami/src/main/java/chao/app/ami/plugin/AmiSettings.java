package chao.app.ami.plugin;

import android.content.Context;
import chao.app.ami.utils.SPUtils;
import java.util.Set;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class AmiSettings {

    private static final String SP_LOG_ENABLED = "log_enabled";

    protected SPUtils spUtils;

    private boolean logEnabled;

    private OnSettingsChangeListener settingsChangeListener;

    public AmiSettings() {
        spUtils = SPUtils.getInstance(getClass().getSimpleName(), Context.MODE_PRIVATE);
        logEnabled = spUtils.getBoolean(SP_LOG_ENABLED);
    }

    public void setLogEnabled(boolean enabled) {
        logEnabled = enabled;
        put(SP_LOG_ENABLED, enabled);
    }

    public boolean logEnabled() {
        return logEnabled;
    }

    public void put(String key, int value) {
        spUtils.put(key, value);
        if (settingsChangeListener != null) {
            settingsChangeListener.onSettingsChanged(key, value);
        }
    }

    public void put(String key, boolean value) {
        spUtils.put(key, value);
        if (settingsChangeListener != null) {
            settingsChangeListener.onSettingsChanged(key, value);
        }
    }

    public void put(String key, String value) {
        spUtils.put(key, value);
        if (settingsChangeListener != null) {
            settingsChangeListener.onSettingsChanged(key, value);
        }
    }

    public void put(String key, float value) {
        spUtils.put(key, value);
        if (settingsChangeListener != null) {
            settingsChangeListener.onSettingsChanged(key, value);
        }
    }

    public void put(String key, Set<String> value) {
        spUtils.put(key, value);
        if (settingsChangeListener != null) {
            settingsChangeListener.onSettingsChanged(key, value);
        }
    }

    public void setSettingsChangeListener(OnSettingsChangeListener listener) {
        this.settingsChangeListener = listener;
    }

    public interface OnSettingsChangeListener {
        <T> void onSettingsChanged(String key, T value);
    }
}
