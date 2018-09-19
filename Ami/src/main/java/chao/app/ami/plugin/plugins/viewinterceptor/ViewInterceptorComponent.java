package chao.app.ami.plugin.plugins.viewinterceptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import chao.app.ami.plugin.AmiGeneralComponent;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/12
 */
public class ViewInterceptorComponent extends AmiGeneralComponent {

    private InterceptorLayerManager manager;

    private ViewInterceptorSettings settings;

    public ViewInterceptorComponent(AmiPlugin plugin) {
        super(plugin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View layout = inflater.inflate(R.layout.ami_plugin_view_interceptor_settings, parent, false);
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
        return layout;
    }



}
