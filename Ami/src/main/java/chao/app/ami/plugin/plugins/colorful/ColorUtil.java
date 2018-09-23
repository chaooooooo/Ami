package chao.app.ami.plugin.plugins.colorful;

import android.graphics.Color;

/**
 * @author qinchao
 * @since 2018/9/19
 */
public class ColorUtil {

    /**
     * Convert HSL (hue-saturation-lightness) components to a RGB color.
     * <ul>
     * <li>hsl[0] is Hue [0 .. 360)</li>
     * <li>hsl[1] is Saturation [0...1]</li>
     * <li>hsl[2] is Lightness [0...1]</li>
     * </ul>
     * If hsv values are out of range, they are pinned.
     *
     * @param hsl 3 element array which holds the input HSL components.
     * @return the resulting RGB color
     */
    public static int[] HSL2RGB(float[] hsl) {
        final float h = hsl[0];
        final float s = hsl[1];
        final float l = hsl[2];

        final float c = (1f - Math.abs(2 * l - 1f)) * s;
        final float m = l - 0.5f * c;
        final float x = c * (1f - Math.abs((h / 60f % 2f) - 1f));

        final int hueSegment = (int) h / 60;

        int r = 0, g = 0, b = 0;

        switch (hueSegment) {
            case 0:
                r = Math.round(255 * (c + m));
                g = Math.round(255 * (x + m));
                b = Math.round(255 * m);
                break;
            case 1:
                r = Math.round(255 * (x + m));
                g = Math.round(255 * (c + m));
                b = Math.round(255 * m);
                break;
            case 2:
                r = Math.round(255 * m);
                g = Math.round(255 * (c + m));
                b = Math.round(255 * (x + m));
                break;
            case 3:
                r = Math.round(255 * m);
                g = Math.round(255 * (x + m));
                b = Math.round(255 * (c + m));
                break;
            case 4:
                r = Math.round(255 * (x + m));
                g = Math.round(255 * m);
                b = Math.round(255 * (c + m));
                break;
            case 5:
            case 6:
                r = Math.round(255 * (c + m));
                g = Math.round(255 * m);
                b = Math.round(255 * (x + m));
                break;
        }

        r = constrain(r, 0, 255);
        g = constrain(g, 0, 255);
        b = constrain(b, 0, 255);
        return new int[]{r, g, b};
    }

    /**
     * Convert HSL (hue-saturation-lightness) components to a RGB color.
     * <ul>
     * <li>hsl[0] is Hue [0 .. 360)</li>
     * <li>hsl[1] is Saturation [0...1]</li>
     * <li>hsl[2] is Lightness [0...1]</li>
     * </ul>
     * If hsv values are out of range, they are pinned.
     *
     * @param hsl 3 element array which holds the input HSL components.
     * @return the resulting RGB color
     */
    public static int HSL2Color(float[] hsl) {
        int[] rgb = HSL2RGB(hsl);
        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Convert the ARGB color to its HSL (hue-saturation-lightness) components.
     * <ul>
     * <li>hsl[0] is Hue [0 .. 360)</li>
     * <li>hsl[1] is Saturation [0...1]</li>
     * <li>hsl[2] is Lightness [0...1]</li>
     * </ul>
     *
     * @param color the ARGB color to convert. The alpha component is ignored.
     * @param hsl 3 element array which holds the resulting HSL components.
     */
    public static void colorToHSL(int color, float[] hsl) {
        RGB2HSL(Color.red(color), Color.green(color), Color.blue(color), hsl);
    }

    /**
     * Convert RGB components to HSL (hue-saturation-lightness).
     * <ul>
     * <li>hsl[0] is Hue [0 .. 360)</li>
     * <li>hsl[1] is Saturation [0...1]</li>
     * <li>hsl[2] is Lightness [0...1]</li>
     * </ul>
     *
     * @param r   red component value [0..255]
     * @param g   green component value [0..255]
     * @param b   blue component value [0..255]
     * @param hsl 3 element array which holds the resulting HSL components.
     */
    public static void RGB2HSL(int r, int g, int b, float[] hsl) {
        final float rf = r / 255f;
        final float gf = g / 255f;
        final float bf = b / 255f;

        final float max = Math.max(rf, Math.max(gf, bf));
        final float min = Math.min(rf, Math.min(gf, bf));
        final float deltaMaxMin = max - min;

        float h, s;
        float l = (max + min) / 2f;

        if (max == min) {
            // Monochromatic
            h = s = 0f;
        } else {
            if (max == rf) {
                h = ((gf - bf) / deltaMaxMin) % 6f;
            } else if (max == gf) {
                h = ((bf - rf) / deltaMaxMin) + 2f;
            } else {
                h = ((rf - gf) / deltaMaxMin) + 4f;
            }

            s = deltaMaxMin / (1f - Math.abs(2f * l - 1f));
        }

        h = (h * 60f) % 360f;
        if (h < 0) {
            h += 360f;
        }

        hsl[0] = constrain(h, 0f, 360f);
        hsl[1] = constrain(s, 0f, 1f);
        hsl[2] = constrain(l, 0f, 1f);
    }

    private static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    private static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}
