package chao.app.debug;

import android.app.Application;
import chao.app.ami.Ami;

/**
 * @author qinchao
 * @since 2018/9/25
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Ami.init(this);
    }
}
