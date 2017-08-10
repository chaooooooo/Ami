package chao.app.ami.utils;

import chao.app.ami.Ami;

/**
 * @author chao.qin
 * @since 2017/8/10
 */

public class DeviceUtil {

    public static int dp2px(int dp){
        float density = Ami.getApp().getResources().getDisplayMetrics().density;
        return (int)(density * dp + 0.5);

    }
}
