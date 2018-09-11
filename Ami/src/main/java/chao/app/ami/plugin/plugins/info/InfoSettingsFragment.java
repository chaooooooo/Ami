package chao.app.ami.plugin.plugins.info;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoSettingsFragment extends AmiPluginFragment implements CompoundButton.OnCheckedChangeListener {


    private CheckBox fpsCheckbox;

    private CheckBox appInfoCheckbox;

    private InfoManager manager;

    private InfoSettings settings;

    public InfoSettingsFragment() {
        settings = getSettings();
        manager = getManager();
    }

    @Override
    public void setupView(View layout) {
        super.setupView(layout);
        fpsCheckbox = (CheckBox) layout.findViewById(R.id.ami_fps_settings);
        appInfoCheckbox = (CheckBox) layout.findViewById(R.id.ami_info_settings_app);

        fpsCheckbox.setChecked(settings.isShowFPS());
        appInfoCheckbox.setChecked(settings.isShowAppInfo());

        fpsCheckbox.setOnCheckedChangeListener(this);
        appInfoCheckbox.setOnCheckedChangeListener(this);
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return InfoPlugin.class;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == fpsCheckbox) {
            settings.setShowFPS(isChecked);
            manager.updateVisible();
        } else if (buttonView == appInfoCheckbox) {
            settings.setShowAppInfo(isChecked);
            manager.updateVisible();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_info_settings_fragment;
    }
}
