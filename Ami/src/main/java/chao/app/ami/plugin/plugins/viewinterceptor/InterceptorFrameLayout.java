package chao.app.ami.plugin.plugins.viewinterceptor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import chao.app.ami.Constants;
import chao.app.ami.utils.DeviceUtil;
import chao.app.ami.utils.ReflectUtil;
import chao.app.debug.R;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class InterceptorFrameLayout extends FrameLayout implements ViewInterceptor.OnViewTouchedListener {

    private static final int ACTION_VIEW_WIDTH = LayoutParams.WRAP_CONTENT;
    private static final int ACTION_VIEW_HEIGHT = LayoutParams.WRAP_CONTENT;

    private static final int SETTINGS_PANEL_MIN_HEIGHT = ViewSettingsPanel.PANEL_HEIGHT;
    private static final int DRAG_BAR_HEIGHT = DeviceUtil.dp2px(40);


    private static final int FIXED_SPACE = DeviceUtil.dp2px(10);


    private ViewInterceptor mInterceptor;

    private Paint mDrawPaint = new Paint();

    private TextPaint mTextPaint = new TextPaint();

    private Paint mTextColorPaint = new Paint();

    private float mTextHeight;

    private RectF mFocusRect = new RectF();
    private boolean mCleanDraw = true;

    private OnTouchedTargetChangeListener mTargetChangeListener;

    private WeakReference<InterceptorRecord> mTouchedRecord;
    private WeakReference<InterceptorRecord> mLastRecord;
    private int[] mTempViewLocation = new int[2];
    private int[] mFrameViewLocation = new int[2];

    private ListView mActionListView;
    private LayoutInflater mInflater;

    private InterceptorFrameLayout.LayoutParams mActionParams;

    private Point mDownPoint = new Point();

    private boolean mSecondClickable = false;


    private ViewSettingsPanel mSettingsPanel;
    private boolean mSettingPanelEnabled = false;
    private View mDragBar;


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

        mSettingsPanel = new ViewSettingsPanel(this);
        LayoutParams settingsParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SETTINGS_PANEL_MIN_HEIGHT);
        addView(mSettingsPanel, settingsParams);
        mDragBar = mSettingsPanel.findViewById(R.id.ami_drag_bar);
        mSettingsPanel.setVisibility(mSettingPanelEnabled ? VISIBLE: GONE);


        mDrawPaint.setColor(Color.RED);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeWidth(4);

        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(DeviceUtil.dp2px(12));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextHeight = mTextPaint.measureText("0");

        mTextColorPaint.setDither(true);
        mTextColorPaint.setTextSize(DeviceUtil.dp2px(12));
        mTextColorPaint.setAntiAlias(true);
        mTextColorPaint.setStyle(Paint.Style.FILL);

        setWillNotDraw(false);
    }

    public void setInterceptor(ViewInterceptor interceptor) {
        mInterceptor = interceptor;
        mInterceptor.setOnViewTouchedListener(this);
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

    /**
     *  touch拦截事件不会影响原有的touch， click或longClick等事件触发
     *  但是有可能会抢夺view的焦点导致原有的事件不触发
     *
     *  @see #mSecondClickable
     */
    @Override
    public boolean onViewTouched(InterceptorRecord record, MotionEvent event) {
        if (!mInterceptor.isInterceptorEnabled()) {
            return false;
        }
        int action = event.getAction();
        boolean result = false;
        if (mLastRecord != null && mLastRecord.get() == record) {
            OnTouchListener touchListener = record.getSourceTouchListener();
            if (touchListener != null) {
                result = touchListener.onTouch(record.view, event);
            }
            return result;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isActionDialogShowed()) {
                    hideActionDialog();
                    return false;
                }
                if (mTouchedRecord != null) {
                    InterceptorRecord lastRecord = mTouchedRecord.get();
                    if (lastRecord == null || lastRecord != record) {
                        if (mTargetChangeListener != null) {
                            mTargetChangeListener.onTouchTargetChanged(record);
                        }
                    }
                }
                mTouchedRecord = new WeakReference<>(record);
                mDownPoint.x = (int) event.getRawX();
                mDownPoint.y = (int) event.getRawY();
            case MotionEvent.ACTION_MOVE:
                mSecondClickable = false;
                InterceptorRecord touchedRecord = mTouchedRecord.get();
                if (touchedRecord == null) {
                    return false;
                }
                getBoundaryOnLayout(touchedRecord.view, mFocusRect);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
//                if (mLastRecord != null && mLastRecord.get() == record) {
//                    mSecondClickable = true;
//                }
//
//                if (mSecondClickable) {
//                    if (performSecondTouchEvent(record, event)) {
//                        result = true;
//                    }
//                    mSecondClickable = false;
//                }
            case MotionEvent.ACTION_CANCEL:
                mLastRecord = new WeakReference<>(record);
                break;

        }
        return false;
    }

    private boolean performSecondTouchEvent(InterceptorRecord record, MotionEvent event) {
        boolean processed = false;
        if (record == null) {
            return false;
        }

        OnTouchListener touchListener = record.getSourceTouchListener();
        if (touchListener != null) {
            processed = touchListener.onTouch(record.view, event);
        }
        OnClickListener clickListener = record.getSourceClickListener();
        if (clickListener != null) {
            Method setPressed = ReflectUtil.getMethod(View.class, "setPressed", boolean.class, float.class, float.class);
            if (setPressed != null) {
                //点击特效
                ReflectUtil.callMethod(setPressed, record.view, true, event.getX(), event.getY());
                //点击事件
                record.view.performClick();
                processed = true;
            }
        }

        if (performSecondTouchEvent(record.getParentRecord(), event)) {
            processed = true;
        }
        return processed;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mInterceptor.isInterceptorEnabled()) {
            return;
        }
        if (mTouchedRecord == null || mTouchedRecord.get() == null) {
            return;
        }
        if (mCleanDraw) {
            mCleanDraw = false;
            return;
        }
        mFocusRect.inset(-4, -4);

        canvas.drawRoundRect(mFocusRect, 10, 10, mDrawPaint);

        super.draw(canvas);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    InterceptorRecord getTouchedRecord() {
        return mTouchedRecord.get();
    }

    public void cleanSelected() {
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
            if (child == mActionListView) {
                layoutActionList();
            } else if (child == mSettingsPanel) {
                layoutSettingsPanel();
            }

        }
    }

    private void layoutSettingsPanel() {
        if (!mSettingPanelEnabled) {
            return;
        }
        int layoutHeight = getMeasuredHeight();
        int layoutWidth = getMeasuredWidth();
        int dragBarHeight = mDragBar.getMeasuredHeight();
        int panelTop = layoutHeight - dragBarHeight;
        mSettingsPanel.layout(0, panelTop, layoutWidth, layoutHeight);
    }

    private void layoutActionList() {
        LayoutParams params = (LayoutParams) mActionListView.getLayoutParams();
        int width = mActionListView.getMeasuredWidth();
        int height = mActionListView.getMeasuredHeight();
        width = Math.min(width, Constants.MAX_LIST_WIDTH);
        height = Math.min(height, Constants.MAX_LIST_HEIGHT);
        int listLeft = params.left;
        int listTop = params.top;

        int listRight = params.left + width;
        int listBottom = params.top + height;

        int layoutWidth = getWidth();
        int layoutHeight = getHeight();
        if (listRight > layoutWidth) {
            listLeft = layoutWidth - width - FIXED_SPACE;
            listRight = listLeft + width;
        }
        if (listBottom > layoutHeight) {
            listTop = layoutHeight - height - FIXED_SPACE;
            listBottom = listTop + height;
        }
        mActionListView.layout(listLeft, listTop, listRight, listBottom);
    }

    public ListView getActionListView() {
        return mActionListView;
    }

    boolean isActionDialogShowed() {
        return mActionListView.getVisibility() == VISIBLE && mActionListView.getParent() != null;
    }

    public void showActionDialog() {
        getLocationOnScreen(mFrameViewLocation);
        mActionParams.left = mDownPoint.x - mFrameViewLocation[0];
        mActionParams.top = mDownPoint.y - mFrameViewLocation[1];
        mActionParams.width = LayoutParams.WRAP_CONTENT;
        mActionParams.height = LayoutParams.WRAP_CONTENT;
        removeView(mActionListView);
        addView(mActionListView, mActionParams);
    }

    public void hideActionDialog() {
        mActionListView.getAdapter();
        removeView(mActionListView);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        int left; //子View相对于InterceptorFrameLayout的x坐标
        int top; //子View相对于InterceptorFrameLayout的y坐标

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public void setOnTouchedTargetListener(OnTouchedTargetChangeListener targetListener) {
        mTargetChangeListener = targetListener;
    }

    public interface OnTouchedTargetChangeListener {
        void onTouchTargetChanged(InterceptorRecord record);
    }
}
