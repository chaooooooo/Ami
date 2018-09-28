package chao.app.ami.utils;

import chao.app.ami.Ami;

/**
 * @author chao.qin
 * @since 2017/8/10
 */

public class DeviceUtil {

    public static int dp2px(int dp){
        return (int)(getDeviceDensity() * dp + 0.5);
    }

    public static int px2sp(float pxValue) {
        final float fontScale = Ami.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = Ami.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



    public static int getDeviceWidth() {
        return Ami.getApp().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight() {
        return Ami.getApp().getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDeviceDensity() {
        return Ami.getApp().getResources().getDisplayMetrics().density;
    }
}
