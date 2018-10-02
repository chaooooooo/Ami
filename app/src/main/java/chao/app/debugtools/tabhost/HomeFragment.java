package chao.app.debugtools.tabhost;

import android.os.Bundle;
import androidx.annotation.Nullable;

import chao.app.ami.annotations.LayoutID;
import chao.app.debugtools.BaseFragment;
import chao.app.debugtools.R;

/**
 * @author chao.qin
 * @since 2017/7/30
 */
@LayoutID(R.layout.home_fragment)
public class HomeFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setBackgroundResource(android.R.color.holo_blue_bright);
    }
}
