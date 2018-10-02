package chao.app.debugtools;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;

/**
 * @author chao.qin
 * @since 2017/8/3
 */
@LayoutID(R.layout.base_fragment)
public class BaseFragment extends AMISupportFragment {
    private View mLayout;
    private int mBackgroundResource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout = super.onCreateView(inflater, container, savedInstanceState);
        if (mBackgroundResource != 0) {
            mLayout.setBackgroundResource(mBackgroundResource);
        }
        return mLayout;
    }

    @Override
    public void setupView(View view) {

    }

    public void setBackgroundResource(int resId) {
        mBackgroundResource = resId;
        if (mLayout != null) {
            mLayout.setBackgroundResource(resId);
        }
    }
}
