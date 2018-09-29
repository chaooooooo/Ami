package chao.app.ami.plugin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author qinchao
 * @since 2018/9/19
 */
public abstract class AmiPluginSettingPane {

    private AmiPlugin mPlugin;

    public AmiPluginSettingPane(AmiPlugin plugin) {
        mPlugin = plugin;
    }

    public CharSequence getTitle(){
        return mPlugin.getTitle();
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup parent);


    protected AmiPlugin getPlugin() {
        return mPlugin;
    }

    @SuppressWarnings("unchecked")
    protected <T extends AmiSettings> T getSettings() {
        return (T) getPlugin().getSettings();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getManager() {
        return (T) getPlugin().getManager();
    }

}
