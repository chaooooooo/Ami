package chao.app.ami.fps;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Choreographer;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class FPSManager extends HandlerThread implements Handler.Callback {

    private static final int HANDLER_FPS_UPDATE = 1;

    private Handler handler;

    private long lastFrameTimeNanos = 0; //ns纳秒 1ms = 1000,000ns

    private int lastFps = 0;

    private OnFPSUpdateListener onFPSUpdateListener;


    public FPSManager(@NonNull OnFPSUpdateListener listener) {
        super("fps manager");
        onFPSUpdateListener = listener;
        handler = new Handler(Looper.getMainLooper(), this);
    }

    private void notifyFPSUpdate(int fps) {
        Message message = handler.obtainMessage(HANDLER_FPS_UPDATE);
        message.arg1 = fps;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == HANDLER_FPS_UPDATE) {
            final int fps = msg.arg1;
            if (fps < 60) {
                onFPSUpdateListener.onFpsUpdate(msg.arg1);
                return true;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onFPSUpdateListener.onFpsUpdate(fps);
                }
            }, 300);
            return true;
        }
        return false;
    }

    @Override
    protected void onLooperPrepared() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Choreographer.getInstance().postFrameCallback(new FPSFrameCallback());
        }
    }


    long thisTime = 0;
    long lastTime = 0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private class FPSFrameCallback implements Choreographer.FrameCallback {
        @Override
        public void doFrame(long frameTimeNanos) {
            thisTime = System.currentTimeMillis();
            if (lastFrameTimeNanos == 0) {
                lastFrameTimeNanos = frameTimeNanos;
                Choreographer.getInstance().postFrameCallback(this);
                return;
            }
            long deltaTimeMillis = (frameTimeNanos - lastFrameTimeNanos);

            int fps = (int) Math.round(1000 /((double)deltaTimeMillis / 1000000));
            if (fps != lastFps) {
                notifyFPSUpdate(fps);
                lastFps = fps;
            }
            lastFrameTimeNanos = frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
        }
    }


    public interface OnFPSUpdateListener {
        void onFpsUpdate(int fps);
    }
}
