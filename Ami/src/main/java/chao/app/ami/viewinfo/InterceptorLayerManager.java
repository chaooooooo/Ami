package chao.app.ami.viewinfo;

import android.content.Context;
import android.graphics.RectF;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import chao.app.ami.text.TextManager;
import chao.app.debug.R;

/**
 * @author chao.qin
 * @since 2017/8/9
 */

public class InterceptorLayerManager implements ViewInterceptor.OnViewLongClickListener {


    private ViewInterceptor mInterceptor = new ViewInterceptor();

    private InterceptorFrameLayout mLayout;

    private InterceptorFrameLayout mInterceptorFrameLayout;

    private ListView mActionListView;
    private ArrayAdapter<String> mActionListAdapter;

    private RectF mActionBoundary = new RectF();


    public InterceptorLayerManager(InterceptorFrameLayout layout) {
        Context context = layout.getContext();
        mLayout = layout;
        mLayout.setInterceptor(mInterceptor);
        mInterceptor.setOnViewLongClickListener(this);
        mActionListView = mLayout.getActionListView();
        mActionListAdapter = new ArrayAdapter<>(context, R.layout.ami_viewinfo_adapter_item, R.id.ami_text);
        mActionListView.setAdapter(mActionListAdapter);

    }

    @Override
    public boolean onViewLongClicked(View view) {
        showAction();
        return true;
    }


    private void showAction() {
        mLayout.showAction();
        mActionListAdapter.clear();
        mActionListAdapter.addAll(TextManager.getSPoetry().getContent());
    }

    private void hideAction() {
        mLayout.removeView(mActionListView);
    }

}
