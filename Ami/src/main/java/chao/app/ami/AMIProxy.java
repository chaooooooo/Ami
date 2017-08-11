package chao.app.ami;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author chao.qin
 * @since 2017/8/11
 */

public class AMIProxy extends Proxy {
    /**
     * Constructs a new {@code Proxy} instance from a subclass
     * (typically, a dynamic proxy class) with the specified value
     * for its invocation handler.
     *
     * @param h the invocation handler for this proxy instance
     */
    protected AMIProxy(InvocationHandler h) {
        super(h);
    }
}
