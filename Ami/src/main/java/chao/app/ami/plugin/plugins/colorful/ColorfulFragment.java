package chao.app.ami.plugin.plugins.colorful;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/19
 */
public class ColorfulFragment extends AmiPluginFragment {

    private TextView indicator;

    private ColorfulSeek hue, situation, lightness;

    private float[] hsl = new float[3];

    @Override
    public void setupView(View layout) {
        super.setupView(layout);
        hue = findView(R.id.ami_plugin_colorful_hue);
        situation = findView(R.id.ami_plugin_colorful_situation);
        lightness = findView(R.id.ami_plugin_colorful_lightness);
        hue.setOnSeekChangeListener(new ColorfulSeek.OnSeekChangeListener() {
            @Override
            public void onValueChanged(ColorfulSeek seekBar, float value) {
                hsl[0] = value;
                update();
            }
        });
        situation.setOnSeekChangeListener(new ColorfulSeek.OnSeekChangeListener() {
            @Override
            public void onValueChanged(ColorfulSeek seekBar, float value) {
                hsl[1] = value;
                update();
            }
        });
        lightness.setOnSeekChangeListener(new ColorfulSeek.OnSeekChangeListener() {
            @Override
            public void onValueChanged(ColorfulSeek seekBar, float value) {
                hsl[2] = value;
                update();
            }
        });

        indicator = findView(R.id.ami_plugin_colorful_indicator);

        hsl[0] = hue.getValue();
        hsl[1] = situation.getValue();
        hsl[2] = lightness.getValue();
        update();
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return ColorfulPlugin.class;
    }

    private void update() {
        int[] rgb = ColorUtil.HSL2RGB(hsl);
        int color = Color.rgb(rgb[0], rgb[1], rgb[2]);
        String red = Integer.toHexString(rgb[0]);
        String green = Integer.toHexString(rgb[1]);
        String blue = Integer.toHexString(rgb[2]);
        String text = "0x" + red + green + blue;
        indicator.setText(text);
        indicator.setBackgroundColor(color);
        if (hsl[2] > 0.5) {
            indicator.setTextColor(Color.BLACK);
        } else {
            indicator.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_colorful_fragment;
    }

}
