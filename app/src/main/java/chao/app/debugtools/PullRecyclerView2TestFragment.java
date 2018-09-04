package chao.app.debugtools;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.debugtools.widgets.cardrefresh.PullHeaderView;
import chao.app.debugtools.widgets.cardrefresh.PullRecycleView;
import chao.app.debugtools.widgets.cardrefresh.PullRecyclerAdapter;
import chao.app.debugtools.widgets.cardrefresh.PullStaggeredGridLayoutManager;

/**
 * @author qinchao
 * @since 2018/8/15
 */

@LayoutID(R.layout.test_pull_recyclerview_fragment)
public class PullRecyclerView2TestFragment extends AMISupportFragment {

    private PullRecycleView recycleView;

    @Override
    public void setupView(View layout) {
        recycleView = findView(R.id.pull_recycler_view);
//        recycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
        PullStaggeredGridLayoutManager layoutManager = new PullStaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
        recycleView.setLayoutManager(layoutManager);


        PullHeaderView headerView = new PullHeaderView(getContext());
        headerView.setCardView(R.layout.test_header_view);
        headerView.setRefreshView(R.layout.test_refresh_view);
        recycleView.setHeaderView(headerView);
        recycleView.setAdapter(new PullRecyclerAdapter(recycleView));

        recycleView.addOnPullStateChangedListener(new PullRecycleView.OnPullStateChangedListener() {
            @Override
            public void onStateChanged(PullRecycleView.State state) {
                if (state == PullRecycleView.State.REFRESHING) {
                    recycleView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recycleView.finishRefreshing();
                        }
                    }, 2000);
                }
            }
        });

    }
}
