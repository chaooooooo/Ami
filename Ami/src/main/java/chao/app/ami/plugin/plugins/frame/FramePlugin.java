package chao.app.ami.plugin.plugins.frame;

import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiGeneralComponent;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class FramePlugin extends AmiPlugin<FramePluginFragment, AmiSettings, AmiGeneralComponent> {


    public FramePlugin() {
    }

    @Override
    public AmiSettings createSettings() {
        return null;
    }

    @Override
    public FramePluginFragment createFragment() {
        return new FramePluginFragment();
    }

    @Override
    public AmiGeneralComponent createComponent() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return "对象";
    }

    @Override
    public Object getManager() {
        return null;
    }

    @Override
    public void onBindView(AmiContentView contentView) {

    }

    public void notifyFrameChanged() {
        if (mFragment != null) {
            mFragment.notifyFrameChanged();
        }
    }
}
