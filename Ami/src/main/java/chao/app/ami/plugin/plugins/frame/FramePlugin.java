package chao.app.ami.plugin.plugins.frame;

import android.support.v4.app.Fragment;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class FramePlugin extends AmiPlugin {


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
    public Object getManager() {
        return null;
    }

    @Override
    public void onBindView(AmiContentView contentView) {

    }

    @Override
    public AmiSettings getSettings() {
        return null;
    }
}
