package chao.app.ami.plugin;

import android.content.Context;
import chao.app.ami.utils.SPUtils;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class AmiSettings {

    protected SPUtils spUtils;

    public AmiSettings() {
        spUtils = SPUtils.getInstance(getClass().getSimpleName(), Context.MODE_PRIVATE);
    }
}
