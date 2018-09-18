package chao.app.ami.plugin;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author qinchao
 * @since 2018/9/18
 */
public class MovementTouch implements View.OnTouchListener {

    private static final int NO_XY = Integer.MAX_VALUE;

    private float x = NO_XY;
    private float y = NO_XY;
    private float lastX = NO_XY;
    private float lastY = NO_XY;

    private View moveView;

    public MovementTouch(View view) {
        moveView = view;
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int action = event.getAction();
        x = event.getRawX();
        y = event.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                moveView.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                updateLocation();
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                updateLocation();
                lastX = x;
                lastY = y;
                break;
        }
        return true;
    }

    private void reset() {
        x = NO_XY;
        y = NO_XY;
        lastX = NO_XY;
        lastY = NO_XY;
    }

    private void updateLocation() {
        if (lastX == NO_XY && lastY == NO_XY) {
            return;
        }
        float dx = x - lastX;
        float dy = y - lastY;

        View parent = (View) moveView.getParent();
        int maxWidth = parent.getWidth();
        int maxHeight = parent.getHeight();

        int viewWidth = moveView.getWidth();
        int viewHeight = moveView.getHeight();

        int left = Math.min(maxWidth - viewWidth, Math.max(0, (int) (moveView.getLeft() + dx)));
        int right = left + moveView.getWidth();
        int top = Math.min(maxHeight - viewHeight, Math.max(0, (int) (moveView.getTop() + dy)));
        int bottom = top + moveView.getHeight();
        moveView.layout(left, top, right, bottom);
    }
}
