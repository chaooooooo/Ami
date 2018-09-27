package chao.app.ami.fps;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Choreographer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class FPSManager extends HandlerThread implements Handler.Callback {

    private static final int HANDLER_FPS_UPDATE = 1;

    private Handler handler;

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private class FPSFrameCallback implements Choreographer.FrameCallback {
        @Override
        public void doFrame(long frameTimeNanos) {
            executorService.execute(new CalcFrameTask(frameTimeNanos));
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ArrayList<Long> frameTimes = new ArrayList<>();

    public class CalcFrameTask implements Runnable {

        long frameTimeNanos;

        public CalcFrameTask(long frameTimeNanos) {
            this.frameTimeNanos = frameTimeNanos;
        }

        @Override
        public void run() {
            int fps;
            while (frameTimes.size() > 0) {
                long first = frameTimes.get(0);
                if (frameTimeNanos - first > 1000 * 1000000) {
                    frameTimes.remove(0);
                } else {
                    break;
                }
            }
            frameTimes.add(frameTimeNanos);
            fps = frameTimes.size();

            if (fps != lastFps) {
                notifyFPSUpdate(fps);
                lastFps = fps;
            }
        }
    }


    public interface OnFPSUpdateListener {
        void onFpsUpdate(int fps);
    }
}
