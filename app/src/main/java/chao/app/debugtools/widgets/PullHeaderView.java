package chao.app.debugtools.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public class PullHeaderView extends LinearLayout implements PullRecycleView.OnPullStateChangedListener, PullRecycleView.OnModeChangedListener {

    private View mCardView;

    private View mRefreshView;

    private int mMode = PullRecycleView.CARD_MODE;

    public PullHeaderView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void addCardView(int layoutId) {
        addCardView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    /**
     */
    public void addCardView(View cardView) {
        this.mCardView = cardView;
        ViewGroup.LayoutParams params = cardView.getLayoutParams();
        mCardView.setLayoutParams(params);
        addView(mCardView, params);
    }

    public View setRefreshView(int layoutId) {
        return setRefreshView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public View setRefreshView(View refreshView) {
        mRefreshView = refreshView;
        ViewGroup.LayoutParams params = refreshView.getLayoutParams();
        mRefreshView.setLayoutParams(params);
        addView(mRefreshView, params);
        return mRefreshView;
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

    @Override
    public void onModeChangedListener(int mode) {
        if (mMode == mode) {
            return;
        }
        mMode = mode;
        if (mode == PullRecycleView.CARD_MODE) {
            removeView(mRefreshView);
            addView(mRefreshView, 0);
        } else if (mode == PullRecycleView.NORMAL_MODEL){
            removeView(mRefreshView);
            addView(mRefreshView, 1);
        }
    }
}
