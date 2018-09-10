package chao.app.ami.plugin.plugins.info;

import android.support.v4.app.Fragment;
import chao.app.ami.plugin.AmiBasePlugin;
import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoPlugin extends AmiBasePlugin {

    private InfoSettings settings;

    public InfoPlugin(InfoSettings settings) {
        this.settings = settings;
    }

    @Override
    protected Fragment createFragment() {
        return new InfoSettingsFragment();
    }

    @Override
    public CharSequence getTitle() {
        return "info";
    }

    @Override
    public AmiSettings getSettings() {
        return settings;
    }
}
