package chao.app.ami;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chao.qin
 * @since 2017/7/30
 */

public class Interceptor<T> implements InvocationHandler {

    private T mTargetListener;

    private OnInterceptorListener mInterceptorListener;

    private boolean mIntercept;

    public interface OnInterceptorListener {
        Object onBeforeInterceptor(Object proxy, Method method, Object[] args);

        Object onAfterInterceptor(Object proxy, Method method, Object[] args);
    }

    private void setOnInterceptorListener(OnInterceptorListener listener) {
        mInterceptorListener = listener;
    }

    private Interceptor(T target) {
        mTargetListener = target;
    }

    public static <T> T newInstance(Class<T> interfaceClass, OnInterceptorListener listener) {
        return (T) newInstance(null, new Class[]{interfaceClass}, listener, false);
    }

    public static <T> T newInstance(T source, Class<T> interfaceClass, OnInterceptorListener listener) {
        return (T) newInstance(source, new Class[]{interfaceClass}, listener, false);
    }

    public static <T> T newInstance(T source, Class<T> interfaces, OnInterceptorListener listener, boolean intercept) {
        return (T) newInstance(source, new Class[]{interfaces}, listener, intercept);
    }


    /**
     *
     * @param source      被拦截事件
     * @param interfaces   被拦截的接口
     * @param listener    回调监听
     * @param intercept   是否拦截被拦截事件
     * @param <T>         被拦截事件类型
     * @return    返回被拦截事件的代理。
     */
    public static <T> T newInstance(T source, Class[] interfaces, OnInterceptorListener listener, boolean intercept) {
        ClassLoader classLoader = null;
        if (listener != null) {
            classLoader = listener.getClass().getClassLoader();
        }
        if (classLoader == null) {
            classLoader = Ami.getApp().getClassLoader();
        }
        Interceptor<T> interceptor = new Interceptor<>(source);
        interceptor.setOnInterceptorListener(listener);
        interceptor.mIntercept = intercept;
        return (T) Proxy.newProxyInstance(classLoader, interfaces, interceptor);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (mInterceptorListener != null) {
            result = mInterceptorListener.onBeforeInterceptor(proxy, method, args);
        }
        if (mTargetListener != null && !mIntercept) {
            Object invoke = method.invoke(mTargetListener, args);
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

    public T getSourceListener() {
        return mTargetListener;
    }
}
