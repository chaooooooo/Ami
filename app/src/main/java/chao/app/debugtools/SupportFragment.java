package chao.app.debugtools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chao.app.ami.base.AMISupportFragment;

/**
 * @author chao.qin
 * @since 2017/3/24
 */

public class SupportFragment extends AMISupportFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("Second support Fragment!");
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Bundle bundle = getArguments();
        int value1 = bundle.getInt("key1");
        String value2 = bundle.getString("key2");
        if (value2 == null) {
            return textView;
        }

        String text = getActivity().getString(R.string.extras_text,"key1", value1, "key2", value2);
        textView.setText(text);
        return textView;
    }

    @Override
    public void setupView(View layout) {

    }
}
