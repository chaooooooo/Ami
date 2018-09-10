package chao.app.ami.plugin.plugins.frame;

import android.support.v4.app.Fragment;
import chao.app.ami.plugin.AmiBasePlugin;
import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class FramePlugin extends AmiBasePlugin {


    public FramePlugin() {
    }

    @Override
    public Fragment createFragment() {
        return new FrameFragment();
    }

    @Override
    public CharSequence getTitle() {
        return "frame";
    }

    @Override
    public AmiSettings getSettings() {
        return null;
    }
}
