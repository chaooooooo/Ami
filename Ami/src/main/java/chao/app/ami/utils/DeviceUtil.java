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
