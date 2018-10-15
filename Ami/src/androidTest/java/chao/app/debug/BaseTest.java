package chao.app.debug;

import android.content.Context;
import chao.app.ami.Ami;

/**
 * @author qinchao
 * @since 2018/9/17
 */
public class BaseTest {

    public BaseTest() {
    }


    public Context getAppContext() {
        return Ami.getApp();
    }
}
