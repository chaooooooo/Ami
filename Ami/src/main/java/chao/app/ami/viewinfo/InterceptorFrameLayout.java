package chao.app.ami.viewinfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.lang.ref.WeakReference;

import chao.app.debug.R;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class InterceptorFrameLayout extends FrameLayout implements ViewGroup.OnHierarchyChangeListener, ViewInterceptor.OnViewTouchedListener {

    private static final int ACTION_VIEW_WIDTH = 300;
    private static final int ACTION_VIEW_HEIGHT = 700;


    private ViewInterceptor mInterceptor;

    private Paint mDrawPaint = new Paint();
    private RectF mFocusRect = new RectF();
    private boolean mCleanDraw = true;

    private WeakReference<View> mTouchedView;
    private int[] mTempViewLocation = new int[2];
    private int[] mFrameViewLocation = new int[2];

    private ListView mActionListView;
    private LayoutInflater mInflater;

    private InterceptorFrameLayout.LayoutParams mActionParams;

    private Point mDownPoint = new Point();


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

        mInflater = LayoutInflater.from(getContext());
        mActionListView = (ListView) mInflater.inflate(R.layout.ami_layout_actions, this, false);

        mActionParams = new InterceptorFrameLayout.LayoutParams(ACTION_VIEW_WIDTH, ACTION_VIEW_HEIGHT);

        mDrawPaint.setColor(Color.RED);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeWidth(4);
        setWillNotDraw(false);
        setOnHierarchyChangeListener(this);
    }

    public void setInterceptor(ViewInterceptor interceptor) {
        mInterceptor = interceptor;
        mInterceptor.setOnViewTouchedListener(this);
        mInterceptor.injectListeners(this);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        if (mInterceptor != null) {
            mInterceptor.injectListeners(child);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        //do nothing
    }

    RectF getBoundaryOnLayout(View view, RectF rectF) {
        view.getLocationOnScreen(mTempViewLocation);
        getLocationOnScreen(mFrameViewLocation);
        int left = mTempViewLocation[0] - mFrameViewLocation[0];
        int top = mTempViewLocation[1] - mFrameViewLocation[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        rectF.set(left, top, right, bottom);
        return rectF;
    }

    @Override
    public void onViewTouched(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchedView = new WeakReference<>(view);
                mDownPoint.x = (int) event.getRawX();
                mDownPoint.y = (int) event.getRawY();
            case MotionEvent.ACTION_MOVE:
                View touchedView = mTouchedView.get();
                if (touchedView == null) {
                    return;
                }
                getBoundaryOnLayout(touchedView, mFocusRect);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                mDownPoint.x = 0;
//                mDownPoint.y = 0;
                break;

        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mCleanDraw) {
            mCleanDraw = false;
            return;
        }
        mFocusRect.inset(-4, -4);

        canvas.drawRoundRect(mFocusRect, 10, 10, mDrawPaint);
    }

    public void cleanFocusDraw() {
        mCleanDraw = true;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != mActionListView) {
                continue;
            }
            LayoutParams params = (LayoutParams) mActionListView.getLayoutParams();
            mActionListView.layout(params.left, params.top, params.left + mActionListView.getWidth(), params.top + mActionListView.getHeight());
        }

    }

    public ListView getActionListView() {
        return mActionListView;
    }

    public void showAction() {
        getLocationOnScreen(mFrameViewLocation);
        mActionParams.left = mDownPoint.x - mFrameViewLocation[0];
        mActionParams.top = mDownPoint.y - mFrameViewLocation[1];
        removeView(mActionListView);
        addView(mActionListView, mActionParams);
    }


    public static class LayoutParams extends FrameLayout.LayoutParams {
        int left; //子View相对于InterceptorFrameLayout的x坐标
        int top; //子View相对于InterceptorFrameLayout的y坐标

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
