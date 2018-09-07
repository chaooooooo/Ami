package chao.app.ami.plugin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author qinchao
 * @since 2018/9/6
 */
public class AmiPluginSettingsPane extends FrameLayout {

    private static final int WHAT_CHANGE_ALPHA = 1;

    private static final float DEFAULT_ALPHA = 0.4f;

    private static final float SHOW_ALPHA = 0.8f;

    private boolean isShow = false;


    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_CHANGE_ALPHA:
                    float alpha = (float) msg.obj;
                    setAlpha(alpha);
                    isShow = false;
                    if(alpha == SHOW_ALPHA) {
                        isShow = true;
                    }
                    break;
            }
            return false;
        }
    });

    public AmiPluginSettingsPane(Context context) {
        super(context);
        init();
    }

    public AmiPluginSettingsPane(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmiPluginSettingsPane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setAlpha(DEFAULT_ALPHA);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isShow) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    notifyChangeAlpha(SHOW_ALPHA, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                notifyChangeAlpha(DEFAULT_ALPHA, 3000);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void cancelChangeAlpha() {
        mHandler.removeMessages(WHAT_CHANGE_ALPHA);
    }

    private void notifyChangeAlpha(float alpha, int delay) {
        cancelChangeAlpha();
        Message message = mHandler.obtainMessage(WHAT_CHANGE_ALPHA);
        message.obj = alpha;
        mHandler.sendMessageDelayed(message, delay);
    }
}
