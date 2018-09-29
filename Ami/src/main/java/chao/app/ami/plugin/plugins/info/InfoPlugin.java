package chao.app.ami.plugin.plugins.info;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.AmiPluginSettingPane;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoPlugin extends AmiPlugin {

    private InfoManager manager;


    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    public AmiPluginSettingPane createComponent() {
        return new InfoPane(this);
    }

    @Override
    public CharSequence getTitle() {
        return "Info";
    }

    @Override
    public Object getManager() {
        return manager;
    }

    @Override
    public void onBindView(AmiContentView contentView) {
        manager = new InfoManager(contentView, (InfoSettings) getSettings());
    }

    @Override
    public AmiSettings createSettings() {
        return new InfoSettings();
    }

    @Override
    public void onActivityChanged(FragmentActivity activity) {
        super.onActivityChanged(activity);
        manager.setupManager(activity);
    }
}
