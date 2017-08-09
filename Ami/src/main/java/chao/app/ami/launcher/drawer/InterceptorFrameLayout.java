package chao.app.ami.launcher.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class InterceptorFrameLayout extends FrameLayout implements ViewGroup.OnHierarchyChangeListener, ViewInterceptor.OnViewTouchedListener {

    private ViewInterceptor mInterceptor = new ViewInterceptor();

    private Paint mDrawPaint = new Paint();
    private RectF mFocusRect = new RectF();
    private boolean mCleanDraw = true;

    public InterceptorFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public InterceptorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterceptorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mInterceptor.setOnViewTouchedListener(this);
        mDrawPaint.setColor(Color.RED);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeWidth(4);
        setWillNotDraw(false);
        setOnHierarchyChangeListener(this);
    }

    public void setInterceptor(ViewInterceptor interceptor) {
        mInterceptor = interceptor;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        mInterceptor.injectListeners(child);
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        //do nothing
    }

    @Override
    public void onViewTouched(View view, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];
            int right= left + view.getWidth();
            int bottom = top + view.getHeight();
            mFocusRect.set(left, top, right, bottom);
            invalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mCleanDraw) {
            mCleanDraw = false;
            return;
        }
        int[] location = new int[2];
        getLocationOnScreen(location);
        RectF rectF = new RectF(mFocusRect);
        rectF.left -= location[0];
        rectF.right -= location[0];
        rectF.top  -= location[1];
        rectF.bottom -= location[1];
        rectF.inset(-4, -4);

        canvas.drawRoundRect(rectF,10, 10, mDrawPaint);
    }

    public void cleanFocusDraw() {
        mCleanDraw = true;
        postInvalidate();
    }
}
