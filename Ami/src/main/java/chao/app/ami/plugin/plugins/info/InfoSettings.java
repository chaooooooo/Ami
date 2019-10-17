package chao.app.ami.plugin.plugins.info;

import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoSettings extends AmiSettings {

    private boolean showAppInfo = spUtils.getBoolean("appInfo", false);

    private boolean showDisplayMetrics = spUtils.getBoolean("displayMetrics", false);

    private boolean showDeviceInfo = spUtils.getBoolean("deviceInfo", false);

    public InfoSettings() {
    }


    public boolean isShowAppInfo() {
        return showAppInfo;
    }

    public void setShowAppInfo(boolean showAppInfo) {
        this.showAppInfo = showAppInfo;
        put("appInfo", showAppInfo);
    }

    public boolean isShowDisplayMetrics() {
        return showDisplayMetrics;
    }

    public void setShowDisplayMetrics(boolean showDisplayMetrics) {
        this.showDisplayMetrics = showDisplayMetrics;
        put("displayMetrics", showDisplayMetrics);
    }

    public boolean isShowDeviceInfo() {
        return showDeviceInfo;
    }

    public void setShowDeviceInfo(boolean showDeviceInfo) {
        this.showDeviceInfo = showDeviceInfo;
        put("deviceInfo", showDeviceInfo);
    }
}
