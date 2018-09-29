package chao.app.ami.plugin.plugins.viewinterceptor;

import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/12
 */
public class ViewInterceptorSettings extends AmiSettings {

    public static final String VIEW_INTERCEPTOR_ENABLED = "view_interceptor_enabled";

    public static final String VIEW_DETAIL_SHOW_ENABLED = "show_view_detail";

    private boolean enabled = spUtils.getBoolean(VIEW_INTERCEPTOR_ENABLED);

    private boolean showViewDetail = spUtils.getBoolean(VIEW_DETAIL_SHOW_ENABLED);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        put(VIEW_INTERCEPTOR_ENABLED, enabled);
    }

    public boolean isShowViewDetail() {
        return showViewDetail;
    }

    public void showViewDetail(boolean show) {
        this.showViewDetail = show;
        put(VIEW_DETAIL_SHOW_ENABLED, show);
    }
}
