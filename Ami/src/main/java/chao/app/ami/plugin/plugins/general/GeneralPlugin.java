package chao.app.ami.plugin.plugins.general;

import android.support.v4.app.Fragment;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.AmiGeneralComponent;

/**
 * @author qinchao
 * @since 2018/9/18
 */
public class GeneralPlugin extends AmiPlugin {
    @Override
    protected Fragment createFragment() {
        return new GeneralFragment();
    }

    @Override
    public AmiGeneralComponent getComponent() {
        return null;
    }

    @Override
    public AmiSettings getSettings() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return "general";
    }

    @Override
    public Object getManager() {
        return null;
    }

    @Override
    public void onBindView(AmiContentView contentView) {

    }
}
