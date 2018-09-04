package chao.app.debugtools.widgets.cardrefresh;

import android.view.View;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public abstract class AbstractModeController implements ModeController{

    PullRecycleView pullRecycleView;

    PullHeaderView headerView;

    View refreshView;

    private static final int HEADER_POSITION = -1;

    private PullStaggeredGridLayoutManager layoutManager;

    private int mScrollState;

    @Override
    public boolean overHeader() {
        int firstPosition = getLayoutManager().findFirstVisibleItemPosition();
        return firstPosition == HEADER_POSITION;
    }

    @Override
    public void setHeaderView(PullHeaderView headerView) {
        this.headerView = headerView;
        refreshView = headerView.getRefreshView();
    }

    public AbstractModeController(PullRecycleView recycleView) {
        this.pullRecycleView = recycleView;
    }

    public PullStaggeredGridLayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = (PullStaggeredGridLayoutManager) pullRecycleView.getLayoutManager();
        }
        return layoutManager;
    }

    @Override
    public int cursor() {
        return headerView.getBottom();
    }

    @Override
    public int offset() {
        return 0;
    }

    boolean isScrollState(int state) {
        return mScrollState == state;
    }

    void setScrollState(int state) {
        mScrollState = state;
    }

    int getHeight() {
        return headerView.getHeight();
    }

    public int getCardHight() {
        return headerView.getCardView().getHeight();
    }

    public int getRefreshHeight() {
        return headerView.getRefreshView().getHeight();
    }
}
