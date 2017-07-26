package chao.app.debug.launcher.drawer;

import android.view.KeyEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author chao.qin
 * @since 2017/7/25
 */

public class ActivityHook implements InvocationHandler {

    private KeyEvent.Callback mCallback;

    public ActivityHook(KeyEvent.Callback callback) {
        mCallback = callback;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("onKeyDown".equals(method.getName())) {

        }
        return method.invoke(proxy,args);
    }
}
