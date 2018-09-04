package chao.app.debugtools.widgets.cardrefresh;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

    private PullHeaderView mHeaderView;

    private ArrayList<OnPullStateChangedListener> mPullStateListeners = new ArrayList<>();

    private ArrayList<OnModeChangedListener> mModeListeners = new ArrayList<>();

    private NormalModeController normalModeController;

    private CardModeController cardModeController;

    private AbstractModeController controller;

    private PullStaggeredGridLayoutManager layoutManager;

    public void finishRefreshing() {
        setState(State.FINISH);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.allShrink();
            }
        }, 500);
    }

    public enum State {
        HIDDEN, PULL, READY_REFRESH, REFRESHING, READY_CARD, CARD,READY_HIDDEN_CARD, SHRINKING, FINISH
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
                if (mode == CARD_MODE) {
                    controller = cardModeController;
                } else {
                    controller = normalModeController;
                }
                layoutManager.setController(controller);

            }
        });
        addOnPullStateChangedListener(new OnPullStateChangedListener() {
            @Override
            public void onStateChanged(State state) {
                if (state != State.READY_HIDDEN_CARD && state != State.SHRINKING) {
                    mHeaderView.getCardView().setAlpha(1.0f);
                }
                Ami.log("state changed： " + state.name());
            }
        });
    }

    public void setLayoutManager(PullStaggeredGridLayoutManager layout) {
        super.setLayoutManager(layout);
        layoutManager = layout;
        layoutManager.setController(controller);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
//        Ami.log("onScrolled: " + dy);
        shrink(dy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private int scrollState;

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        scrollState = state;
        controller.setScrollState(state);
        shrink(0);
    }

    private void shrink(int dy) {
        Ami.log("shrink ===> dy = " + dy + ", scrollState = " + scrollState + ", state = " + mState.name() + ", cursor = " + controller.cursor() + ", mode = " + mMode);
        if (!controller.overHeader()) {
            setMode(NORMAL_MODEL);
            setState(State.HIDDEN);
            return;
        }
        if (layoutManager.getOrientation() != RecyclerView.VERTICAL) {
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
                int y = mHeaderView.getHeight() - controller.cursor() - mHeaderView.getRefreshView().getHeight();
                smoothScrollBy(0, -y);
                mTouchable = false;
                return;
            }

            if (controller.overFull()) {
                setState(State.READY_CARD);
            } else if(controller.overRefresh()) {
                setState(State.READY_REFRESH);
            } else if(controller.overPull()){
                setState(State.PULL);
            } else if (isState(State.READY_HIDDEN_CARD)) {
                if (isMode(CARD_MODE)) {
                    cardModeController.readyHiddenCard();
                }
            } else if (controller.overCard()){
                setState(State.READY_HIDDEN_CARD);
            }  else {
                setState(State.HIDDEN);
            }
        } else if (scrollState == SCROLL_STATE_IDLE) {
            if (mState == State.READY_CARD) {
                setState(PullRecycleView.State.CARD);
                setMode(CARD_MODE);
                smoothScrollBy(0, -controller.cursor());
            } else if (mState == State.READY_REFRESH) {
                controller.showRefresh();
                setState(PullRecycleView.State.REFRESHING);
            } else if (mState == State.READY_HIDDEN_CARD) {
                controller.hideCard();
            } else if (mState == State.PULL || mState == State.HIDDEN || mState == State.FINISH) {
                controller.allShrink();
            }
        } else if (scrollState == SCROLL_STATE_SETTLING) {
            if (controller.overHeader() && dy < 0) {
                if (!isState(State.SHRINKING) && !isState(State.REFRESHING)) {
                    stopScroll();
                    if (!isState(State.REFRESHING)) {
                        controller.allShrink();
                    }
                }
            }
        }
        if (isState(State.REFRESHING) && scrollState != SCROLL_STATE_DRAGGING) {
            controller.resetRefresh();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            if (!mTouchable) {
                mTouchable = true;
            }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
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
                    int y = mHeaderView.getHeight() - controller.cursor() - mHeaderView.getRefreshView().getHeight();
                    stopScroll();
                    scrollBy(0, -y);
                    mTouchable = false;
                    return true;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    public boolean isState(State state) {
        return mState == state;
    }

    public void setState(State state) {
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
        Ami.log("setMode " + mMode + " --> " + mode);
        if (mMode == mode) {
            return;
        }
        mMode = mode;
        for (OnModeChangedListener listener: mModeListeners) {
            listener.onModeChangedListener(mode);
        }
    }

    public void setHeaderView(PullHeaderView headerView) {
        this.mHeaderView = headerView;
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        normalModeController.setHeaderView(mHeaderView);
        cardModeController.setHeaderView(mHeaderView);
        addOnPullStateChangedListener(mHeaderView);
        addOnModeChangedListener(mHeaderView);
    }

    public PullHeaderView getHeaderView() {
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

    public ModeController getController() {
        return controller;
    }
}
