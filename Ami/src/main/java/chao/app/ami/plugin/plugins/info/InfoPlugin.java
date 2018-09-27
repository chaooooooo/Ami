package chao.app.ami.plugin.plugins.info;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.AmiGeneralComponent;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoPlugin extends AmiPlugin {

    private InfoSettings settings;

    private InfoManager manager;


    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    public AmiGeneralComponent createComponent() {
        return new InfoComponent(this);
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
        settings = new InfoSettings();
        manager = new InfoManager(contentView, settings);
    }

    @Override
    public AmiSettings getSettings() {
        return settings;
    }

    @Override
    public void onActivityChanged(FragmentActivity activity) {
        super.onActivityChanged(activity);
        manager.setupManager(activity);
    }
}
