package chao.app.ami;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chao.qin
 * @since 2017/7/30
 */

public class Interceptor<T> implements InvocationHandler {

    private T mTarget;

    private OnInterceptorListener mInterceptorListener;

    public interface OnInterceptorListener {
        Object onBeforeInterceptor(Object proxy, Method method, Object[] args);
        Object onAfterInterceptor(Object proxy, Method method, Object[] args);
    }

    private void setOnInterceptorListener(OnInterceptorListener listener) {
        mInterceptorListener = listener;
    }

    private Interceptor(T target) {
        mTarget = target;
    }

    public static <T> T newInstance(Class<T> interfaceClass, OnInterceptorListener listener) {
        return (T) newInstance(null, new Class[]{interfaceClass}, listener);
    }

    public static <T> T newInstance(T source, Class<T> interfaceClass, OnInterceptorListener listener) {
       return (T) newInstance(source, new Class[]{interfaceClass}, listener);
    }

    public static <T> T newInstance(T source, Class<T>[] interfaces, OnInterceptorListener listener) {
        ClassLoader classLoader = null;
        if (source != null) {
            classLoader = source.getClass().getClassLoader();
        }
        if (classLoader == null) {
            classLoader = Ami.getApp().getClassLoader();
        }
        Interceptor<T> interceptor = new Interceptor<>(source);
        interceptor.setOnInterceptorListener(listener);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, interceptor);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (mInterceptorListener != null) {
            result = mInterceptorListener.onBeforeInterceptor(proxy, method, args);
        }
        if (mTarget != null) {
            Object invoke = method.invoke(mTarget, args);
            if (invoke != null) {
                result = invoke;
            }
        }
        if (mInterceptorListener != null) {
            Object after = mInterceptorListener.onAfterInterceptor(proxy, method, args);
            if (after != null) {
                result = after;
            }
        }
        return result;
    }
}
