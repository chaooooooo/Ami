package chao.app.ami.plugin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author qinchao
 * @since 2018/9/18
 */
public class MovementTouch extends ViewGroup implements View.OnTouchListener {

    private static final int NO_XY = Integer.MAX_VALUE;

    private float x = NO_XY;
    private float y = NO_XY;
    private float lastX = NO_XY;
    private float lastY = NO_XY;

    private View moveView;
    private int left, right, top, bottom;

    public MovementTouch(View view) {
        super(view.getContext());
        moveView = view;
        ViewGroup parentView = (ViewGroup) moveView.getParent();
        parentView.removeView(moveView);
        parentView.addView(this, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LayoutParams layoutParams = moveView.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            left = ((MarginLayoutParams) layoutParams).leftMargin;
            top = ((MarginLayoutParams) layoutParams).topMargin;
        }
        addView(moveView, new LayoutParams(layoutParams));
    }

    public MovementTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
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

        int viewWidth = moveView.getMeasuredWidth();
        int viewHeight = moveView.getMeasuredHeight();

        left = Math.min(maxWidth - viewWidth, Math.max(0, (int) (moveView.getLeft() + dx)));
        top = Math.min(maxHeight - viewHeight, Math.max(0, (int) (moveView.getTop() + dy)));
        right = left + viewWidth;
        bottom = top + viewHeight;
        moveView.layout(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(moveView, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int viewWidth = moveView.getMeasuredWidth();
        int viewHeight = moveView.getMeasuredHeight();
        right = left + viewWidth;
        bottom = top + viewHeight;
        moveView.layout(left, top, right, bottom);
    }
}
