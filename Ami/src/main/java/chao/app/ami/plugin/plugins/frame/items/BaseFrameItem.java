package chao.app.ami.plugin.plugins.frame.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chao.app.ami.utils.AnnotationUtil;

/**
 * @author chao.qin
 * @since 2018/8/11
 */

public abstract class BaseFrameItem implements FrameItem {

    private Context mContext;

    private View mItemView;

    public BaseFrameItem(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getLayoutId() {
        return AnnotationUtil.getLayoutFromAnnotation(getClass());
    }

    @Override
    public View getItemView() {
        return mItemView;
    }

    @Override
    public void initView(ViewGroup parent) {
        mItemView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
    }

    @Override
    public <T extends View> T findViewById(int resId) {
        return (T) mItemView.findViewById(resId);
    }
}
