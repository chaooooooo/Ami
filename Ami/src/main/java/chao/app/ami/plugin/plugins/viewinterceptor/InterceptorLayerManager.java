package chao.app.ami.plugin.plugins.viewinterceptor;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import chao.app.ami.Ami;
import chao.app.ami.Constants;
import chao.app.debug.R;
import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/9
 */

public class InterceptorLayerManager implements ViewInterceptor.OnViewLongClickListener, AdapterView.OnItemClickListener {

    private static final int ACTION_ID_LONG_CLICK = 0;
    private static final int ACTION_ID_TEXT_INJECT = 1;
    private static final int ACTION_ID_VIEW_DETAIL = 2;


    private ViewInterceptor mInterceptor = new ViewInterceptor();

    private InterceptorFrameLayout mLayout;

    private InterceptorFrameLayout mInterceptorFrameLayout;

    private FrameLayout mActionLayout;

    private ListView mActionListView;
    private ArrayAdapter<Action> mActionListAdapter;

    private RectF mActionBoundary = new RectF();

    private ArrayList<Action> mActionList = new ArrayList<>();

    public InterceptorLayerManager() {
        Context context = Ami.getApp();
        mLayout = new InterceptorFrameLayout(context);
        mLayout.setInterceptor(mInterceptor);
        mInterceptor.setOnViewLongClickListener(this);
        mActionListView = mLayout.getActionListView();
        mActionListAdapter = new ArrayAdapter<>(context, R.layout.ami_viewinfo_adapter_item, R.id.ami_text);
        mActionListView.setAdapter(mActionListAdapter);
        mActionListView.setDivider(new ColorDrawable(context.getResources().getColor(R.color.transparent_red)));
        mActionListView.setDividerHeight(1);
        mActionListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onViewLongClicked(InterceptorRecord record) {
        showAction(record.view);
        return true;
    }


    public InterceptorFrameLayout getLayout() {
        return mLayout;
    }

    private void showAction(View view) {
        mActionList.clear();
        mActionList.add(new Action(ACTION_ID_LONG_CLICK, Constants.AMI_ACTION_LONG_CLICK));
        if (view instanceof TextView) {
            mActionList.add(new Action(ACTION_ID_TEXT_INJECT, "注入文本"));
        }
        mActionList.add(new Action(ACTION_ID_VIEW_DETAIL,Constants.AMI_ACTION_VIEW_DETAIL));
        mLayout.showActionDialog();
        mActionListAdapter.clear();
        mActionListAdapter.addAll(mActionList);
    }

    private void hideAction() {
        mActionList.clear();
        mActionListAdapter.clear();
        mLayout.hideActionDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Action action = mActionListAdapter.getItem(position);
        if (action == null) {
            return;
        }
        InterceptorRecord touchedRecord = mLayout.getTouchedRecord();
        if (touchedRecord == null) {
            return;
        }
        switch (action.actionId) {
            case ACTION_ID_LONG_CLICK:
                View.OnLongClickListener listener = touchedRecord.getSourceLongClickListener();
                if (listener != null) {
                    listener.onLongClick(touchedRecord.view);
                }
                break;
            case ACTION_ID_TEXT_INJECT:
                break;
            case ACTION_ID_VIEW_DETAIL:
                break;
        }
        hideAction();
    }

    public void cleanSelected() {
        mLayout.cleanSelected();
    }

    public void injectListeners(ViewGroup vg, View child) {
        mInterceptor.injectListeners(vg, child);
    }

    private static class Action {

        private String name;
        private int actionId;

        public Action(int actionId, String name) {
            this.name = name;
            this.actionId = actionId;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    public void setInterceptorEnabled(boolean enabled) {
        mInterceptor.setInterceptorEnabled(enabled);
    }
}
