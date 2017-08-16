package chao.app.debugtools;

import android.view.View;
import android.widget.TextView;

import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMIFragment;

/**
 * @author chao.qin
 * @since 2017/8/2
 */
@LayoutID(R.layout.debug_info_fragment)
public class DebugInfoFragment extends AMIFragment {

    private String text;
    private TextView mText;

    @Override
    public void setupView(View layout) {
        mText = findView(R.id.text);
        text = mText.getText().toString();
    }
}
