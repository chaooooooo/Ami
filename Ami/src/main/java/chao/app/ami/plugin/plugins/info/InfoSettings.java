package chao.app.ami.plugin.plugins.info;

import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoSettings extends AmiSettings {

    private boolean showFPS = spUtils.getBoolean("fps", true);

    private boolean showAppInfo = spUtils.getBoolean("appInfo", false);

    public InfoSettings() {
    }


    public boolean isShowFPS() {
        return showFPS;
    }

    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
        spUtils.put("fps", showFPS);
    }

    public boolean isShowAppInfo() {
        return showAppInfo;
    }

    public void setShowAppInfo(boolean showAppInfo) {
        this.showAppInfo = showAppInfo;
        spUtils.put("appInfo", showAppInfo);
    }
}
