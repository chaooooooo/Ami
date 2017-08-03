package chao.app.debugtools.tabhost;

import android.os.Bundle;
import android.support.annotation.Nullable;

import chao.app.debugtools.BaseFragment;


/**
 * @author chao.qin
 * @since 2017/7/30
 */

public class MessageFragment extends BaseFragment {
    private int backgroundResource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackgroundResource(android.R.color.holo_blue_dark);
    }

    public void setBackgroundResource(int backgroundResource) {

    }
}
