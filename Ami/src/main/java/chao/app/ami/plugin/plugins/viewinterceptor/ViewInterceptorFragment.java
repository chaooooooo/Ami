package chao.app.ami.plugin.plugins.viewinterceptor;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/12
 */
public class ViewInterceptorFragment extends AmiPluginFragment {

    private InterceptorLayerManager manager;

    private ViewInterceptorSettings settings;

    @Override
    public void setupView(View layout) {
        super.setupView(layout);
        settings = getSettings();
        manager = getManager();
        final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.ami_plugin_view_interceptor_enable);
        checkBox.setChecked(settings.isEnabled());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setEnabled(isChecked);
                manager.setInterceptorEnabled(isChecked);
            }
        });
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return ViewInterceptorPlugin.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_view_interceptor_settings;
    }
}
