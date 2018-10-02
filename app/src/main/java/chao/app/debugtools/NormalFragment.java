package chao.app.debugtools;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chao.app.ami.base.AMIFragment;

/**
 * @author chao.qin
 * @since 2017/3/24
 */

public class NormalFragment extends AMIFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("Second Fragment!");
        return textView;
    }

    @Override
    public void setupView(View layout) {

    }
}
