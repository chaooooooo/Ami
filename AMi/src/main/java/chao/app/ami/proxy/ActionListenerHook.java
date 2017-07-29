package chao.app.ami.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import chao.app.ami.utils.debug;

/**
 * @author chao.qin
 * @since 2017/7/27
 */

public class ActionListenerHook implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        debug.log("invoke :" + method.getName() + ": " + Arrays.toString(args));
        return null;
    }
}
