package chao.app.ami.hooks;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.Window;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chao.qin
 * @since 2017/7/26
 */

public class WindowCallbackHook implements InvocationHandler {

    private Window.Callback mCallback;
    private DispatchKeyEventListener mKeyEventListener;

    private WindowCallbackHook(Window.Callback callback, DispatchKeyEventListener listener) {
        mCallback = callback;
        mKeyEventListener = listener;
    }

    public static Window.Callback newInstance(Activity activity, DispatchKeyEventListener listener) {
        return (Window.Callback) Proxy.newProxyInstance(activity.getClassLoader(), new Class[]{Window.Callback.class}, new WindowCallbackHook(activity,listener));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("dispatchKeyEvent".equals(method.getName())) {
            if (mKeyEventListener.onDispatchKeyEvent((KeyEvent) args[0])) {
                return true;
            }
        }
        return method.invoke(mCallback, args);
    }


    public interface DispatchKeyEventListener {
        boolean onDispatchKeyEvent(KeyEvent keyEvent);
    }
}
