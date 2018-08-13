package chao.app.ami.frames.items;

import android.view.View;
import android.view.ViewGroup;

import chao.app.ami.frames.IFrame;

/**
 * @author chao.qin
 * @since 2018/8/11
 */

public interface FrameItem {

    void bindView();

    void bindData(IFrame.Entry entry);

    int getLayoutId();

    View getItemView();

    void initView(ViewGroup parent);

    <T extends View> T findViewById(int resId);
}
