package chao.app.ami.base;

import android.view.View;

import chao.app.debug.R;

/**
 * @author chao.qin
 * @since 2017/7/31
 */
public abstract class AmiSimpleFragment extends AMIFragment implements View.OnClickListener {

    @Override
    public void setupView(View layout) {
        findView(R.id.ami_simple_btn).setOnClickListener(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_simple_fragment;
    }
}
