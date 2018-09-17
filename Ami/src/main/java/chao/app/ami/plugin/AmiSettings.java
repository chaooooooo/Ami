package chao.app.ami.plugin;

import android.content.Context;
import chao.app.ami.utils.SPUtils;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class AmiSettings {

    private static final String SP_LOG_ENABLED = "log_enabled";

    protected SPUtils spUtils;

    private boolean logEnabled;

    public AmiSettings() {
        spUtils = SPUtils.getInstance(getClass().getSimpleName(), Context.MODE_PRIVATE);
        logEnabled = spUtils.getBoolean(SP_LOG_ENABLED);
    }

    public void setLogEnabled(boolean enabled) {
        logEnabled = enabled;
        spUtils.put(SP_LOG_ENABLED, enabled);
    }

    public boolean logEnabled() {
        return logEnabled;
    }
}
