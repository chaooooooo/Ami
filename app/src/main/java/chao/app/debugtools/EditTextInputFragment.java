package chao.app.debugtools;

import android.view.View;
import android.widget.EditText;
import chao.app.ami.Ami;
import chao.app.ami.UI;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;

/**
 * @author chao.qin
 * @since 2017/8/1
 */
@LayoutID(R.layout.edit_input_fragment)
public class EditTextInputFragment extends AMISupportFragment{


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Ami.log(hasFocus);
        }
    };

    @Override
    public void setupView(View layout) {
        EditText username = findView(R.id.username);
        EditText password = findView(R.id.password);
        EditText check_code = findView(R.id.check_code);
        username.setOnFocusChangeListener(focusChangeListener);
        password.setOnFocusChangeListener(focusChangeListener);
        check_code.setOnFocusChangeListener(focusChangeListener);

        findView(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        findView(R.id.jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UI.show(getActivity(), PasswordFragment.class);
            }
        });
    }
}
