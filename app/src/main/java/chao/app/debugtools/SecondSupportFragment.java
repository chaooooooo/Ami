package chao.app.debugtools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chao.app.ami.base.BaseSupportFragment;

/**
 * @author chao.qin
 * @since 2017/3/24
 */

public class SecondSupportFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("Second support Fragment!");
        Bundle bundle = getArguments();
        int value1 = bundle.getInt("key1");
        String value2 = bundle.getString("key2");
        Log.e("chao.qin","value1 = " + value1);
        Log.e("chao.qin","value2 = " + value2);
        return textView;
    }
}
