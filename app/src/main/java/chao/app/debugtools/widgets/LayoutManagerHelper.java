package chao.app.debugtools.widgets;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public class LayoutManagerHelper {

    private RecyclerView.LayoutManager mLayoutManager;

    public LayoutManagerHelper(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public int findFirstVisibleItemPosition() {
        if (mLayoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) mLayoutManager;
            return sglm.findFirstVisibleItemPositions(new int[sglm.getSpanCount()])[0];
        } else {
            throw new IllegalStateException("");
        }
    }

    public View findViewByPosition(int position) {
        return mLayoutManager.findViewByPosition(position);
    }

    public int getOrientation() {
        if (mLayoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager)mLayoutManager).getOrientation();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) mLayoutManager;
            return sglm.getOrientation();
        } else {
            throw new IllegalStateException("");
        }
    }
}
