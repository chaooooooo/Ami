package chao.app.ami.plugin.plugins.colorful;

import android.support.v4.app.Fragment;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPluginSettingPane;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/19
 */
public class ColorfulPlugin extends AmiPlugin {
    @Override
    protected Fragment createFragment() {
        return new ColorfulFragment();
    }

    @Override
    public AmiPluginSettingPane createComponent() {
        return null;
    }

    @Override
    public AmiSettings createSettings() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return "取色";
    }

    @Override
    public Object getManager() {
        return null;
    }

    @Override
    public void onBindView(AmiContentView contentView) {

    }
}
