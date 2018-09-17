package chao.app.debug;



import static chao.app.ami.Ami.log;


import android.view.MotionEvent;
import android.view.View;
import chao.app.ami.Interceptor;
import java.lang.reflect.Method;
import org.junit.Test;

/**
 * @author qinchao
 * @since 2018/9/17
 */
public class InterceptorTest extends BaseTest{

    @Test
    public void testInterceptor() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log();
            }
        };

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                log();
                return false;
            }
        };

        View.OnClickListener hookListener;

        hookListener = Interceptor.newInstance(clickListener, View.OnClickListener.class, clickInterceptor, false);
        hookListener.onClick(null);

        hookListener = Interceptor.newInstance(clickListener, View.OnClickListener.class, clickInterceptor, true);
        hookListener.onClick(null);

        hookListener = Interceptor.newInstance(clickListener, View.OnClickListener.class, null, false);
        hookListener.onClick(null);

        hookListener = Interceptor.newInstance(clickListener, View.OnClickListener.class, null, true);
        hookListener.onClick(null);


        View.OnTouchListener hookTouchListener;
        hookTouchListener = Interceptor.newInstance(touchListener, View.OnTouchListener.class, touchInterceptor, false);
        hookTouchListener.onTouch(null, null);

        hookTouchListener = Interceptor.newInstance(touchListener, View.OnTouchListener.class, null, false);
        hookTouchListener.onTouch(null, null);

        hookTouchListener = Interceptor.newInstance(touchListener, View.OnTouchListener.class, null, true);
        hookTouchListener.onTouch(null, null);


    }

    private Interceptor.OnInterceptorListener touchInterceptor = new Interceptor.OnInterceptorListener<Boolean>() {
        @Override
        public Boolean onBeforeInterceptor(Object proxy, Method method, Object[] args) {
            log();
            return null;
        }

        @Override
        public Boolean onAfterInterceptor(Object proxy, Method method, Object[] args, Boolean result) {
            log();
            return null;
        }
    };

    private Interceptor.OnInterceptorListener clickInterceptor = new Interceptor.OnInterceptorListener() {
        @Override
        public Object onBeforeInterceptor(Object proxy, Method method, Object[] args) {
            log();
            return null;
        }

        @Override
        public Object onAfterInterceptor(Object proxy, Method method, Object[] args, Object result) {
            log();
            return null;
        }
    };

}
