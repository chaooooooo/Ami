package chao.app.ami.plugin.plugins.general;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.ami.plugin.AmiPluginSettingPane;
import chao.app.ami.utils.DeviceUtil;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/18
 */
public class GeneralFragment extends AmiPluginFragment {

    private LinearLayout mLayout;

    @Override
    public void setupView(View layout) {
        super.setupView(layout);
        mLayout = findView(R.id.ami_plugin_general_parent);
        for (AmiPlugin plugin: mPluginManager.getPlugins()) {
            AmiPluginSettingPane generalComponent = plugin.getComponent();
            if (generalComponent == null) {
                continue;
            }
            LayoutInflater inflater = getLayoutInflater();
            View componentView = generalComponent.onCreateView(inflater, mLayout);
            addComponentView(generalComponent, componentView);
        }
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return GeneralPlugin.class;
    }

    public ViewGroup getLayout() {
        return mLayout;
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_general_fragment;
    }

    public void addComponentView(AmiPluginSettingPane component, View view) {
        TextView titleView = new TextView(getContext());
        titleView.setText(component.getTitle());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        titleView.setTextColor(Color.WHITE);
        titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        titleView.setBackgroundColor(Color.GRAY);
        int hpadding = DeviceUtil.dp2px(16);
        int vpadding = DeviceUtil.dp2px(4);
        titleView.setPadding(hpadding, vpadding, hpadding, vpadding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayout.addView(titleView, layoutParams);

        mLayout.addView(view, layoutParams);
    }
}
