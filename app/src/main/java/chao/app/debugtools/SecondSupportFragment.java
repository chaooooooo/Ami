package chao.app.debugtools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author chao.qin
 * @since 2017/3/24
 */

public class SecondSupportFragment extends Fragment {

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
