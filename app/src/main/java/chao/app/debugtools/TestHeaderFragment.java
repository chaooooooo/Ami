package chao.app.debugtools;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.debugtools.widgets.PullHeaderView2;

/**
 * @author qinchao
 * @since 2018/8/19
 */

@LayoutID(R.layout.test_header_fragment)
public class TestHeaderFragment extends AMISupportFragment {
    @Override
    public void setupView(View layout) {

        PullHeaderView2 headerView = new PullHeaderView2(getContext());
        headerView.setBackgroundColor(Color.RED);
        headerView.setCardView(R.layout.test_header_view);
        headerView.setRefreshView(R.layout.test_refresh_view);
        ViewGroup root = (ViewGroup) layout;
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(headerView);
    }
}
