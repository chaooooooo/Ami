package chao.app.ami.plugin.plugins.viewinterceptor;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import chao.app.ami.utils.DeviceUtil;
import chao.app.debug.R;


public class ViewSettingsPanel extends FrameLayout {

    public static final int PANEL_HEIGHT = DeviceUtil.dp2px(300);
    private static final int DRAG_BAR_HEIGHT = DeviceUtil.dp2px(40);

    private float mLastY = -1;
    private View mDragBar;
    private View mParentView;
    private int mMaxTop;
    private int mMinTop;

    public ViewSettingsPanel(@NonNull View parentView) {
        super(parentView.getContext());
        mParentView = parentView;
        init(parentView.getContext());
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ami_settings_panel, this, false);
        mDragBar = view.findViewById(R.id.ami_drag_bar);
        addView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastY;
                mLastY = y;
                int oldTop = getTop();
                int top = (int) (oldTop + deltaY);
                int parentHeight = mParentView.getHeight();
                int dragbarHeight = mDragBar.getHeight();
                mMaxTop = parentHeight - dragbarHeight;
                mMinTop = parentHeight - PANEL_HEIGHT;
                if (top > mMaxTop || top < mMinTop) {
                    return false;
                }
                setTop(top);
                return false;
        }
        return super.onTouchEvent(event);
    }
}
