package chao.app.debugtools.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import chao.app.ami.Ami;
import java.util.ArrayList;

/**
 * @author qinchao
 * @since 2018/8/15
 */

public class PullRecycleView extends RecyclerView {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static final int NORMAL_MODEL = 1;

    public static final int CARD_MODE = 2;

    private  State mState = State.HIDDEN;

    private int mMode = CARD_MODE;

    private boolean mTouchable = true;

    private PullHeaderView2 mHeaderView;

    private LayoutManagerHelper mLayoutManagerHelper;

    private ArrayList<OnPullStateChangedListener> mPullStateListeners = new ArrayList<>();

    private ArrayList<OnModeChangedListener> mModeListeners = new ArrayList<>();

    private NormalModeController normalModeController;

    private CardModeController cardModeController;

    private AbstractModeController controller;

    private PullStaggeredGridLayoutManager layoutManager;

    public void resetCardMode() {
        Ami.log("resetCardMode");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.allShrink();
            }
        }, 300);
    }

    public void finishRefreshing() {
        setState(State.PULL);
        controller.allShrink();
    }


    public enum State {
        HIDDEN, PULL, READY_REFRESH, REFRESHING, READY_CARD, CARD
    }
    public PullRecycleView(Context context) {
        this(context, null);
    }

    public PullRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecycleView(final Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        normalModeController = new NormalModeController(this);
        cardModeController = new CardModeController(this);
        controller = cardModeController;

        addOnModeChangedListener(new OnModeChangedListener() {
            @Override
            public void onModeChangedListener(int mode) {
                Ami.log("mode changed: " + mode);
                int y = controller.getY();
                if (mode == CARD_MODE) {
                    controller = cardModeController;
                } else {
                    controller = normalModeController;
                }
                controller.setY(y);
                layoutManager.setController(controller);

            }
        });
        addOnPullStateChangedListener(new OnPullStateChangedListener() {
            @Override
            public void onStateChanged(State state) {
                Ami.log("state chagned=" + state.name() + ", mode=" + mMode);
            }
        });
    }

    public void setLayoutManager(PullStaggeredGridLayoutManager layout) {
        super.setLayoutManager(layout);
        layoutManager = layout;
        mLayoutManagerHelper = new LayoutManagerHelper(layout);
        normalModeController.setLayoutManager(mLayoutManagerHelper);
        cardModeController.setLayoutManager(mLayoutManagerHelper);
        layoutManager.setController(controller);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
//        Ami.log("onScrolled: dx=" + dx + ", dy="+dy + ", mOffset=" + controller.offset());
        shrink();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        shrink();
    }

    private int scrollState;

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        scrollState = state;
        controller.setScrollState(state);
//        Ami.log("onScrollStateChanged： scroll state: " + scrollState + ", mode="+mMode + ", state=" + mState.name());
        shrink();
    }

    private void shrink() {
        if (controller.headerShow()) {
            setMode(NORMAL_MODEL);
            return;
        }
        if (mLayoutManagerHelper.getOrientation() != RecyclerView.VERTICAL) {
            return;
        }
        if (scrollState == SCROLL_STATE_DRAGGING) {
            if (isState(State.REFRESHING)) {
                //刷新状态下， 不随着手的拖动而改变状态
                return;
            }
            if (mState == State.READY_CARD) {
                setState(PullRecycleView.State.CARD);
                setMode(CARD_MODE);
                smoothScrollBy(0, -controller.offset());
                mTouchable = false;
                return;
            }

            if (controller.overFull()) {
                setState(State.READY_CARD);
            } else if(controller.overRefresh()) {
                setState(State.READY_REFRESH);
            } else if(controller.overPull()){
                setState(State.PULL);
            } else {
                setState(State.HIDDEN);
            }
        } else if (scrollState == SCROLL_STATE_IDLE) {
            if (mState == State.READY_CARD) {
//                controller.showCard();
                setState(PullRecycleView.State.CARD);
                setMode(CARD_MODE);
                smoothScrollBy(0, -controller.offset());
            } else if (mState == State.READY_REFRESH) {
                controller.showRefresh();
                setState(PullRecycleView.State.REFRESHING);
            } else if (mState == State.PULL || mState == State.HIDDEN) {
                controller.allShrink();
                setState(PullRecycleView.State.HIDDEN);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
//        Ami.log("onInterceptTouchEvent: " + e.toString().substring(15, 40));
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            if (!mTouchable) {
                mTouchable = true;
            }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        Ami.log("onTouchEvent: " + e.toString().substring(15, 40));
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mTouchable) {
                    mTouchable = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mTouchable) {
                    scrollBy(0, -controller.offset());
                    stopScroll();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    private boolean isState(State state) {
        return mState == state;
    }

    private void setState(State state) {
        if (mState == state) {
            return;
        }
        mState = state;
        onStateChanged(state);

        for (OnPullStateChangedListener listener: mPullStateListeners) {
            listener.onStateChanged(state);
        }
    }

    private void onStateChanged(State state) {

    }

    private boolean isMode(int mode) {
        return mMode == mode;
    }

    private void setMode(int mode) {
        if (mMode == mode) {
            return;
        }
        mMode = mode;
        for (OnModeChangedListener listener: mModeListeners) {
            listener.onModeChangedListener(mode);
        }
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
//        Ami.log("onNestedScrollAccepted: child=" + child + ", target="+target + ", axes=" + axes);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        Ami.log("onOverScrolled: scrollX=" + scrollX + ", scrollY="+scrollY + ", clampedX=" + clampedX + ", clampedY=" + clampedY);
    }

    public void setHeaderView(PullHeaderView2 headerView) {
        this.mHeaderView = headerView;
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        normalModeController.setHeaderView(mHeaderView);
        cardModeController.setHeaderView(mHeaderView);
        addOnPullStateChangedListener(mHeaderView);
        addOnModeChangedListener(mHeaderView);
    }

    public PullHeaderView2 getHeaderView() {
        return mHeaderView;
    }

    public void addOnPullStateChangedListener(OnPullStateChangedListener onPullStateChangedListener) {
        mPullStateListeners.add(onPullStateChangedListener);
    }

    public void addOnModeChangedListener(OnModeChangedListener listener) {
        mModeListeners.add(listener);
    }

    public interface OnPullStateChangedListener {
        void onStateChanged(State state);
    }

    public interface OnModeChangedListener {
        void onModeChangedListener(int mode);
    }
}
