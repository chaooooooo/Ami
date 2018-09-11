package chao.app.ami.plugin;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import chao.app.ami.base.AMISupportFragment;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class AmiPluginFragment extends AMISupportFragment {

    @Override
    public void setupTitle() {
    }

    @Override
    public void setupView(View layout) {
        int defaultBg = ResourcesCompat.getColor(getResources(), R.color.common_background_color, null);
        layout.setBackgroundColor(defaultBg);
    }

    protected IPlugin getPlugin(Class plugin) {
        AmiPluginManager pluginManager = AmiPluginManager.getInstance();
        return pluginManager.getPlugin(plugin);
    }

    protected AmiSettings getSettings(Class plugin) {
        return getPlugin(plugin).getSettings();
    }

    protected Object getManager(Class plugin) {
        return getPlugin(plugin).getManager();
    }
}
