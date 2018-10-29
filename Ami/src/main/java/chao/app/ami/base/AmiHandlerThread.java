package chao.app.ami.base;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class AmiHandlerThread extends HandlerThread {

    private Handler mHandler;

    public AmiHandlerThread() {
        super("ami handler thread");
    }


    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(getLooper());
    }

    public Handler getHandler() {
        return mHandler;
    }

}
