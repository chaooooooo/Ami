package chao.app.ami.plugin.plugins.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiGeneralComponent;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/19
 */
public class InfoComponent extends AmiGeneralComponent implements CompoundButton.OnCheckedChangeListener {

    private CheckBox fpsCheckbox;

    private CheckBox appInfoCheckbox;

    private CheckBox logEnabledCheckbox;

    private InfoManager manager;

    private InfoSettings settings;


    public InfoComponent(AmiPlugin plugin) {
        super(plugin);
        settings = getSettings();
        manager = getManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View layout = inflater.inflate(R.layout.ami_plugin_info_settings_fragment, parent, false);

        fpsCheckbox = (CheckBox) layout.findViewById(R.id.ami_fps_settings);
        appInfoCheckbox = (CheckBox) layout.findViewById(R.id.ami_info_settings_app);
        logEnabledCheckbox = (CheckBox) layout.findViewById(R.id.ami_info_settings_log);

        fpsCheckbox.setChecked(settings.isShowFPS());
        appInfoCheckbox.setChecked(settings.isShowAppInfo());
        logEnabledCheckbox.setChecked(settings.logEnabled());

        fpsCheckbox.setOnCheckedChangeListener(this);
        appInfoCheckbox.setOnCheckedChangeListener(this);
        logEnabledCheckbox.setOnCheckedChangeListener(this);

        return layout;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == fpsCheckbox) {
            settings.setShowFPS(isChecked);
            manager.updateVisible();
        } else if (buttonView == appInfoCheckbox) {
            settings.setShowAppInfo(isChecked);
            manager.updateVisible();
        } else if (buttonView == logEnabledCheckbox) {
            settings.setLogEnabled(isChecked);
        }
    }
}
