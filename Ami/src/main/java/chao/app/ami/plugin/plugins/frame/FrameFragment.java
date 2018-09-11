package chao.app.ami.plugin.plugins.frame;

import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class FrameFragment extends AmiPluginFragment {

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_frame_layout;
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return FramePlugin.class;
    }
}
