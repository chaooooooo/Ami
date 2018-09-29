package chao.app.ami.plugin.plugins.viewinterceptor;

import static android.view.View.GONE;
import static android.view.View.NO_ID;
import static android.view.View.VISIBLE;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import chao.app.ami.Ami;
import chao.app.ami.Constants;
import chao.app.ami.plugin.AmiPluginManager;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.MovementLayout;
import chao.app.ami.utils.DeviceUtil;
import chao.app.debug.R;
import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/9
 */

public class InterceptorLayerManager implements ViewInterceptor.OnViewLongClickListener, AdapterView.OnItemClickListener, InterceptorFrameLayout.OnTouchedTargetChangeListener, AmiSettings.OnSettingsChangeListener {

    private static final int ACTION_ID_LONG_CLICK = 0;
    private static final int ACTION_ID_TEXT_INJECT = 1;
    private static final int ACTION_ID_VIEW_DETAIL = 2;

    private static final String LINE_SEPARATOR = "\n";

    private final ViewInterceptorSettings mSettings;


    private ViewInterceptor mInterceptor;

    private InterceptorFrameLayout mLayout;

    private ListView mActionListView;

    private ArrayAdapter<Action> mActionListAdapter;

    private TextView mViewDescriptionView;


    private ArrayList<Action> mActionList = new ArrayList<>();

    public InterceptorLayerManager() {
        Context context = Ami.getApp();
        mLayout = new InterceptorFrameLayout(context);
        mInterceptor = new ViewInterceptor();
        mLayout.setInterceptor(mInterceptor);
        mInterceptor.setOnViewLongClickListener(this);
        mActionListView = mLayout.getActionListView();
        mActionListAdapter = new ArrayAdapter<>(context, R.layout.ami_viewinfo_adapter_item, R.id.ami_text);
        mActionListView.setAdapter(mActionListAdapter);
        mActionListView.setDivider(new ColorDrawable(context.getResources().getColor(R.color.transparent_red)));
        mActionListView.setDividerHeight(1);
        mActionListView.setOnItemClickListener(this);

        mViewDescriptionView = new TextView(context);
        mViewDescriptionView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mViewDescriptionView.setLineSpacing(10, 1);
        MovementLayout movementLayout = new MovementLayout(mLayout);
        movementLayout.addView(mViewDescriptionView);
        mLayout.setOnTouchedTargetListener(this);

        ViewInterceptorPlugin plugin = AmiPluginManager.getPlugin(ViewInterceptorPlugin.class);
        mSettings = plugin.getSettings();
        mSettings.setSettingsChangeListener(this);

    }

    @Override
    public boolean onViewLongClicked(InterceptorRecord record) {
        showAction(record.view);
        return true;
    }

    private String getTextDetails(View view) {

        int[] boundary = new int[2];
        view.getLocationOnScreen(boundary);
        int left = boundary[0];
        int top = boundary[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();

        StringBuilder buffer = new StringBuilder();
        buffer.append(view.getClass().getName()).append("@")
            .append(Integer.toHexString(System.identityHashCode(view))).append(LINE_SEPARATOR)
            .append(view.getWidth()).append(" x ").append(view.getHeight()).append(" (")
            .append(left).append(",").append(top).append(" - ").append(right).append(",").append(bottom).append(")").append(LINE_SEPARATOR)
            .append((view.getId() == NO_ID ? "NO_ID" : getIdName(view))).append(LINE_SEPARATOR);

        if (!(view instanceof TextView)) {
            return buffer.toString();
        }
        TextView textView = (TextView) view;
        String viewText = textView.getText().toString();
        int textSize = (int) textView.getTextSize();
        int textSizeSp = DeviceUtil.px2sp(textSize);
        int textColor = textView.getTextColors().getDefaultColor();

        return buffer.append("Text: ").append(viewText).append(LINE_SEPARATOR)
            .append("Text Size: ").append(textSize).append("px ").append(textSizeSp).append("sp").append(LINE_SEPARATOR)
            .append("Text Color: #").append(Integer.toHexString(textColor)).append(LINE_SEPARATOR)
            .toString();
    }

    private String getIdName(View view) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("#").append(Integer.toHexString(view.getId()));
        int id = view.getId();
        final Resources r = view.getResources();
        boolean hasPackage = (id >>> 24) != 0;
        if (id > 0 && hasPackage && r != null) {
            try {
                String pkgname;
                switch (id&0xff000000) {
                    case 0x7f000000:
                        pkgname="app";
                        break;
                    case 0x01000000:
                        pkgname="android";
                        break;
                    default:
                        pkgname = r.getResourcePackageName(id);
                        break;
                }
                String typename = r.getResourceTypeName(id);
                String entryname = r.getResourceEntryName(id);
                buffer.append(" ");
                buffer.append(pkgname);
                buffer.append(":");
                buffer.append(typename);
                buffer.append("/");
                buffer.append(entryname);
            } catch (Resources.NotFoundException ignored) {
            }
        }
        return buffer.toString();
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

    @Override
    public void onTouchTargetChanged(InterceptorRecord record) {
        if (!mSettings.isShowViewDetail()) {
            return;
        }
        int textColor = Color.BLUE;
        if (record.view instanceof TextView) {
            textColor =  ((TextView) record.view).getTextColors().getDefaultColor();
        }
        mViewDescriptionView.setTextColor(textColor);

        mViewDescriptionView.setText(getTextDetails(record.view));
    }

    @Override
    public void onSettingsChanged(String key, Object value) {
        if (ViewInterceptorSettings.VIEW_DETAIL_SHOW_ENABLED.equals(key)) {
            boolean bValue = (boolean) value;
            int visible = bValue ? VISIBLE: GONE;
            mViewDescriptionView.setVisibility(visible);
        }
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
        mLayout.setVisibility(enabled?View.VISIBLE:View.GONE);
    }
}
