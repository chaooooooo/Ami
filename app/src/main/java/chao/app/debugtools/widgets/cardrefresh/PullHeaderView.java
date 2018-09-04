package chao.app.debugtools.widgets.cardrefresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * @author qinchao
 * @since 2018/8/19
 */

public class PullHeaderView extends ViewGroup implements PullRecycleView.OnPullStateChangedListener, PullRecycleView.OnModeChangedListener {

    private View mCardView;

    private View mRefreshView;

    private boolean isCardMode = true;

    public PullHeaderView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isCardMode) {
            layoutCardMode(l, t, r, b);
        } else {
            layoutNormanMode(l, t, r, b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isCardMode) {
            measureCardMode(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureNormalMode(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureNormalMode(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(mRefreshView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mCardView, widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = Math.max(mRefreshView.getMeasuredWidth(), mCardView.getMeasuredWidth());
        int measureHeight = mRefreshView.getMeasuredHeight() + mCardView.getMeasuredHeight();
//        int measureHeight = mCardView.getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measureHeight);
    }

    private void measureCardMode(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(mRefreshView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mCardView, widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = Math.max(mRefreshView.getMeasuredWidth(), mCardView.getMeasuredWidth());
        int measureHeight = mRefreshView.getMeasuredHeight() + mCardView.getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measureHeight);
    }

    @SuppressWarnings("unused")
    private void layoutNormanMode(int l, int t, int r, int b) {
        int cardViewWidth = mCardView.getMeasuredWidth();
        int cardViewHeight = mCardView.getMeasuredHeight();
        int refreshViewWidth = mRefreshView.getMeasuredWidth();
        int refreshViewHeight = mRefreshView.getMeasuredHeight();

        int left = 0;
        int top = getMeasuredHeight() - cardViewHeight - refreshViewHeight + offset;
        int right = left + cardViewWidth;
        int bottom = top + cardViewHeight + offset;
        mCardView.layout(left, top, right, bottom);

        left = l;
        top = bottom;
        right = left + refreshViewWidth;
        bottom = top + refreshViewHeight;
        mRefreshView.layout(left, top, right, bottom);
    }

    @SuppressWarnings("unused")
    private void layoutCardMode(int l, int t, int r, int b) {
        int cardViewHeight = mCardView.getMeasuredHeight();
        int cardViewWidth = mCardView.getMeasuredWidth();
        int refreshViewHeight = mRefreshView.getMeasuredHeight();
        int refreshViewWidth = mRefreshView.getMeasuredWidth();

        int left = 0;
        int top = 0;
        int right = left + refreshViewWidth;
        int bottom = top + refreshViewHeight;
        mRefreshView.layout(left, top, right, bottom);


        left = l;
        top = bottom;
        right = left + cardViewWidth;
        bottom = top + cardViewHeight;
        mCardView.layout(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        super.draw(canvas);
    }

    public void setRefreshView(int layoutId) {
        setRefreshView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    @SuppressWarnings("unused")
    public void setCardView(int layoutId) {
        setCardView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public void setCardView(View cardView) {
        this.mCardView = cardView;
        LayoutParams params = cardView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        mCardView.setLayoutParams(params);
        addView(mCardView);
        
    }

    public void setRefreshView(View refreshView) {
        this.mRefreshView = refreshView;
        LayoutParams params = refreshView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        mRefreshView.setLayoutParams(params);
        addView(mRefreshView);
    }

    public View getCardView() {
        return mCardView;
    }

    public View getRefreshView() {
        return mRefreshView;
    }

    @Override
    public void onStateChanged(PullRecycleView.State state) {

    }

    private int offset = 0;

    @Override
    public void onModeChangedListener(int mode) {
        final boolean cardMode = mode == PullRecycleView.CARD_MODE;
        if (cardMode == isCardMode) {
            return;
        }
        mCardView.setAlpha(1.0f);
        if (!isCardMode) {
            int start = mCardView.getBottom();
            int end = mRefreshView.getBottom();
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, end - start);
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    offset = (int) animation.getAnimatedValue();
                    requestLayout();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    offset = 0;
                    isCardMode = true;
                    requestLayout();
                }
            });
            valueAnimator.start();
        } else {
            isCardMode = false;
            requestLayout();
        }
    }
}
