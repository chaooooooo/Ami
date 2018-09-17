package chao.app.debugtools;

import chao.app.ami.Ami;

/**
 * @author qinchao
 * @since 2018/9/14
 */
public class DebugApplication extends AppApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Ami.log();
    }
}
