package chao.app.debugtools.widgets;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author qinchao
 * @since 2018/8/20
 */

public class PullStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private AbstractModeController mController;

    public PullStaggeredGridLayoutManager(Context context, int orientation) {
//        super(context, orientation, false);
        super(1, orientation);
    }

    public PullStaggeredGridLayoutManager(int span, int orientation) {
        super(span, orientation);
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

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consumed = dy / 2;
        int realConsumed = dy;
        if (mController.overHeader() && mController.isScrollState(RecyclerView.SCROLL_STATE_DRAGGING)) {
            int unconsumed = dy - consumed;
            realConsumed = super.scrollVerticallyBy(unconsumed, recycler, state);
            consumed += realConsumed;
            mController.deltaY(realConsumed);
        } else {
            consumed = super.scrollVerticallyBy(dy,recycler,state);
            mController.deltaY(consumed);
        }

//        Ami.log("scrollVerticallyBy. dy=" + dy + ", consumed: " + consumed + ", realConsumed: " + realConsumed);
        return consumed;
    }

    @Override
    public void scrollToPosition(int position) {
        position += 1;//position = 0 位置是header
        super.scrollToPosition(position);
        mController.setY(mController.headerHeight());
    }

    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        position += 1;//position = 0 位置是header
        super.scrollToPositionWithOffset(position, offset);
        mController.setY(mController.headerHeight() + offset);
    }
}
