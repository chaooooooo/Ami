package chao.app.ami.base;

import android.os.HandlerThread;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class AmiHandlerThread extends HandlerThread {


    public AmiHandlerThread() {
        super("ami handler thread");
    }


    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
    }

}
