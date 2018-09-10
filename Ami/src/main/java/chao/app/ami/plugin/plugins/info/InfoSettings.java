package chao.app.ami.plugin.plugins.info;

import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoSettings extends AmiSettings {

    private InfoManager manager;

    private boolean showFPS = spUtils.getBoolean("fps", true);

    private boolean showAppInfo = spUtils.getBoolean("appInfo", false);

    public InfoSettings(InfoManager infoManager) {
        manager = infoManager;
    }


    public boolean isShowFPS() {
        return showFPS;
    }

    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
        spUtils.put("fps", showFPS);
        manager.updateVisible();
    }

    public boolean isShowAppInfo() {
        return showAppInfo;
    }

    public void setShowAppInfo(boolean showAppInfo) {
        this.showAppInfo = showAppInfo;
        spUtils.put("appInfo", showAppInfo);
        manager.updateVisible();
    }
}
