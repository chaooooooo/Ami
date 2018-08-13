package chao.app.debug;

import android.text.TextUtils;

import org.junit.Test;

import chao.app.ami.utils.SystemPropertiesUtil;

/**
 * @author chao.qin
 * @since 2018/8/10
 */

public class SystemPropertiesUtilTest {

    @Test
    public void testSystemPropertiesUtil() {
        assert !TextUtils.isEmpty(SystemPropertiesUtil.getString("ro.product.board", ""))
                || !TextUtils.isEmpty(SystemPropertiesUtil.getString("ro.product.brand", ""))
                || !TextUtils.isEmpty(SystemPropertiesUtil.getString("persist.debug.coresight.config", ""))
                || !TextUtils.isEmpty(SystemPropertiesUtil.getString("ro.build.version.sdk", ""));

        assert SystemPropertiesUtil.getInt("ro.build.version.sdk", 0) > 0
                || SystemPropertiesUtil.getInt("vold.post_fs_data_done", -1) >= 0
                || SystemPropertiesUtil.getInt("ro.hwui.text_large_cache_height", 0) > 0;

        assert SystemPropertiesUtil.getBoolean("ro.qualcomm.bluetooth.ftp", false)
                || SystemPropertiesUtil.getBoolean("ro.qualcomm.bluetooth.hfp", false)
                || SystemPropertiesUtil.getBoolean("ro.qualcomm.bluetooth.hsp", false)
                || SystemPropertiesUtil.getBoolean("ro.qualcomm.bluetooth.nap", false)
                || SystemPropertiesUtil.getBoolean("ro.qualcomm.bluetooth.pbap", false)
                || SystemPropertiesUtil.getBoolean("ro.qualcomm.bluetooth.opp", false)
                || SystemPropertiesUtil.getBoolean("use.qti.sw.alac.decoder", false)
                || SystemPropertiesUtil.getBoolean("video.disable.ubwc", false)
                || SystemPropertiesUtil.getBoolean("voice.voip.conc.disabled", false);

        assert SystemPropertiesUtil.getLong("ro.hwui.text_large_cache_height", 0) > 0
                || SystemPropertiesUtil.getLong("ro.hwui.text_large_cache_width", 0) > 0
                || SystemPropertiesUtil.getLong("ro.hwui.text_small_cache_height", 0) > 0
                || SystemPropertiesUtil.getLong("ro.hwui.text_small_cache_width", 0) > 0
                || SystemPropertiesUtil.getLong("ro.build.version.sdk", 0) > 0;

    }
}
