package chao.app.debugtools.widgets;

import android.view.View;
import chao.app.ami.Ami;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public abstract class AbstractModeController implements ModeController{

    PullRecycleView pullRecycleView;

    LayoutManagerHelper layoutManagerHelper;

    PullHeaderView2 headerView;

    View refreshView;

    View cardView;

    int mY;

    int mScrollState;
    @Override
    public boolean overHeader() {
        return offset() < 0;
    }

    @Override
    public void setLayoutManager(LayoutManagerHelper layoutManagerHelper) {
        this.layoutManagerHelper = layoutManagerHelper;
    }

    @Override
    public void setHeaderView(PullHeaderView2 headerView) {
        this.headerView = headerView;
        refreshView = headerView.getRefreshView();
        cardView = headerView.getCardView();
    }

    public AbstractModeController(PullRecycleView recycleView) {
        this.pullRecycleView = recycleView;
    }


    @Override
    public boolean headerShow() {
        return mY > headerView.getHeight();
    }

    @Override
    public void deltaY(int dy) {
//        Ami.log("mY=" + mY  + ", headHeight=" + headerView.getHeight() + ", cardView=" + cardView.getHeight() + ", refreshView=" + refreshView.getHeight());
        mY += dy;
    }

    @Override
    public void allShrink() {
        Ami.log("allShrink");
        if (!overHeader()) {
            return;
        }
        pullRecycleView.smoothScrollBy(0, -offset());
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        mY = y;
    }

    void setScrollState(int state) {
        mScrollState = state;
    }

    boolean isScrollState(int state) {
        return mScrollState == state;
    }

    public int headerHeight() {
        return headerView.getHeight();
    }
}
