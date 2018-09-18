package chao.app.ami.command.beans;

import android.util.DisplayMetrics;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class Screen {

    private int width;
    private int height;
    private float xdpi;
    private float ydpi;
    private int densityDpi;
    private float density;

    private double inch;

    private String densityType;

    private enum Density {
        ldpi(120), mdpi(160), hdpi(240), xhdpi(320), xxhdpi(480), xxxhdpi(640), unknown(0);

        private float density;

        Density(float density) {
            this.density = density;
        }

        public static Density indexOf(float density) {
            for (Density d: values()) {
                if (d.density == density) {
                    return d;
                }
            }
            unknown.density = density;
            return unknown;
        }
    }

    public Screen(DisplayMetrics dm) {
        width = dm.widthPixels;
        height = dm.heightPixels;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;
        density = dm.density;
        densityType = Density.indexOf(densityDpi).name();
        inch = Math.sqrt(width*width/(xdpi*xdpi) + height*height/(ydpi*ydpi));
    }

    @Override
    public String toString() {
        return "Screen{" +
            "width=" + width +
            ", height=" + height +
            ", xdpi=" + xdpi +
            ", ydpi=" + ydpi +
            ", density=" + density +
            ", inch=" + inch +
            ", densityType='" + densityType  +
            "(" + densityDpi + ")" +
            '}';
    }
}