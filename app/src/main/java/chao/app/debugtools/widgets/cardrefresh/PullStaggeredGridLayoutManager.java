package chao.app.debugtools.widgets.cardrefresh;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author qinchao
 * @since 2018/8/20
 */

public class PullStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private AbstractModeController mController;

    public PullStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public void setController(AbstractModeController controller) {
        mController = controller;
    }


    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        //do nothing
        //覆盖掉StaggeredGridLayoutManager的onDetachedFromWindow
        //原onDetachedFromWindow会导致刷新位置错误
    }

    private static final int DAMPING = 2;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consumed = 0;
        int realConsumed;
        if (mController.overHeader() && mController.isScrollState(RecyclerView.SCROLL_STATE_DRAGGING)) {
            consumed = dy / DAMPING;
            int unconsumed = dy - consumed;
            realConsumed = super.scrollVerticallyBy(unconsumed, recycler, state);
            consumed += realConsumed;
        } else {
            consumed = super.scrollVerticallyBy(dy,recycler,state);
        }

        return consumed;
    }


    @Override
    public void scrollToPosition(int position) {
        position += 1;//position = 0 位置是header
        super.scrollToPosition(position);
//        mController.setY(mController.headerHeight());
    }

    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        position += 1;//position = 0 位置是header
        super.scrollToPositionWithOffset(position, offset);
    }

    @Override
    public int[] findFirstVisibleItemPositions(int[] into) {
        int[] positions = super.findFirstVisibleItemPositions(into);
        for (int i=0; i < positions.length; i++) {
            positions[i] -= 1;
        }
        return positions;
    }

    public int findFirstVisibleItemPosition() {
        return findFirstVisibleItemPositions(new int[getSpanCount()])[0];
    }
}
