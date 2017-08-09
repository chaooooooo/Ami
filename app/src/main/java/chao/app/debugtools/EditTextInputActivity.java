package chao.app.debugtools;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMIActivity;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

@LayoutID(R.layout.edit_input_fragment)
public class EditTextInputActivity extends AMIActivity implements View.OnClickListener {

    @Override
    public void setupView(Bundle savedInstanceState) {
        LinearLayout content = findView(R.id.edit_input_content);
//        content.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
