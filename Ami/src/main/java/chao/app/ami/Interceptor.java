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

    public interface OnInterceptorListener<T> {
        T onBeforeInterceptor(Object proxy, Method method, Object[] args);

        T onAfterInterceptor(Object proxy, Method method, Object[] args, T result);
    }

    private void setOnInterceptorListener(OnInterceptorListener listener) {
        mInterceptorListener = listener;
    }

    private Interceptor(T target) {
        mTargetListener = target;
    }

    public static <T> T newInstance(T source, Class interfaceClass, OnInterceptorListener listener) {
        return newInstance(source, new Class[]{interfaceClass}, listener, false);
    }

    @SuppressWarnings("unused")
    public static <T> T newInstance(T source, Class<T> interfaces, OnInterceptorListener listener, boolean intercept) {
        return newInstance(source, new Class[]{interfaces}, listener, intercept);
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
    @SuppressWarnings("all")
    public static <T> T newInstance(T source, Class[] interfaces, OnInterceptorListener listener, boolean intercept) {
        ClassLoader classLoader = null;
        if (listener != null) {
            classLoader = listener.getClass().getClassLoader();
        }
        if (classLoader == null && source != null) {
            classLoader = source.getClass().getClassLoader();
        }
        if (classLoader == null && interfaces[0] != null) {
            classLoader = interfaces[0].getClassLoader();
        }
        Interceptor<T> interceptor = new Interceptor<>(source);
        interceptor.setOnInterceptorListener(listener);
        interceptor.mIntercept = intercept;
        return (T) Proxy.newProxyInstance(classLoader, interfaces, interceptor);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Ami.log(method);
        Object result = null;
        if (mInterceptorListener != null) {
            result = mInterceptorListener.onBeforeInterceptor(proxy, method, args);
        }
        if (mTargetListener != null && !mIntercept) {
            result = method.invoke(mTargetListener, args);
        }
        if (mInterceptorListener != null) {
            result = mInterceptorListener.onAfterInterceptor(proxy, method, args, result);
        }
        Class returnType = method.getReturnType();
        if (result == null) {
            if (returnType == boolean.class) {
                return false;
            } else if (returnType == int.class
                || returnType == short.class
                || returnType == byte.class
                || returnType == char.class
                || returnType == long.class
                || returnType == float.class
                || returnType == double.class
                || Number.class.isAssignableFrom(returnType)) {
                return 0;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked | unused")
    public static <T> T getSourceListener(Object hookListener) {
        if (hookListener == null) {
            return null;
        }
        Interceptor interceptor;
        if (Proxy.isProxyClass(hookListener.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(hookListener);
            if (handler instanceof Interceptor) {
                interceptor = (Interceptor) handler;
                return (T) interceptor.mTargetListener;
            }
        }
        return (T) hookListener;
    }
}
