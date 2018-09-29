package chao.app.ami.plugin.plugins.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginSettingPane;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/19
 */
public class InfoPane extends AmiPluginSettingPane implements CompoundButton.OnCheckedChangeListener {

    private CheckBox appInfoCheckbox;

    private CheckBox logEnabledCheckbox;

    private CheckBox displayMetricsCheckBox;

    private InfoManager manager;

    private InfoSettings settings;


    public InfoPane(AmiPlugin plugin) {
        super(plugin);
        settings = getSettings();
        manager = getManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View layout = inflater.inflate(R.layout.ami_plugin_info_settings_fragment, parent, false);

        appInfoCheckbox = (CheckBox) layout.findViewById(R.id.ami_info_settings_app);
        logEnabledCheckbox = (CheckBox) layout.findViewById(R.id.ami_info_settings_log);
        displayMetricsCheckBox = (CheckBox) layout.findViewById(R.id.ami_info_settings_display);

        appInfoCheckbox.setChecked(settings.isShowAppInfo());
        logEnabledCheckbox.setChecked(settings.logEnabled());
        displayMetricsCheckBox.setChecked(settings.isShowDisplayMetrics());

        appInfoCheckbox.setOnCheckedChangeListener(this);
        logEnabledCheckbox.setOnCheckedChangeListener(this);
        displayMetricsCheckBox.setOnCheckedChangeListener(this);

        return layout;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == appInfoCheckbox) {
            settings.setShowAppInfo(isChecked);
        } else if (buttonView == logEnabledCheckbox) {
            settings.setLogEnabled(isChecked);
        } else if (buttonView == displayMetricsCheckBox) {
            settings.setShowDisplayMetrics(isChecked);
        }
    }
}
