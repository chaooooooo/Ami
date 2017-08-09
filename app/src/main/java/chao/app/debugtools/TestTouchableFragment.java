package chao.app.debugtools;

import android.view.View;

import chao.app.ami.Ami;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;

/**
 * @author chao.qin
 * @since 2017/8/9
 */
@LayoutID(R.layout.test_touchable_fragment)
public class TestTouchableFragment extends AMISupportFragment implements View.OnClickListener {
    @Override
    public void setupView(View layout) {
        findView(R.id.lll).setOnClickListener(this
        );
    }

    @Override
    public void onClick(View v) {
        Ami.log("test onclick");
    }
}
