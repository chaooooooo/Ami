package chao.app.ami.plugin.plugins.viewinterceptor;

import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/12
 */
public class ViewInterceptorSettings extends AmiSettings {

    private static final String VIEW_INTERCEPTOR_ENABLED = "view_interceptor_enabled";

    private boolean enabled = spUtils.getBoolean(VIEW_INTERCEPTOR_ENABLED);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        spUtils.put(VIEW_INTERCEPTOR_ENABLED, enabled);
    }
}
