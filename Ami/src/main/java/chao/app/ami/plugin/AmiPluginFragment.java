package chao.app.ami.plugin;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import chao.app.ami.base.AMISupportFragment;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public abstract class AmiPluginFragment extends AMISupportFragment {

    @Override
    public void setupTitle() {
    }

    @Override
    public void setupView(View layout) {
        int defaultBg = ResourcesCompat.getColor(getResources(), R.color.common_background_color, null);
        layout.setBackgroundColor(defaultBg);
    }

    public abstract Class<? extends AmiPlugin> bindPlugin();

    protected AmiPlugin getPlugin() {
        AmiPluginManager pluginManager = AmiPluginManager.getInstance();
        return pluginManager.getPlugin(bindPlugin());
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