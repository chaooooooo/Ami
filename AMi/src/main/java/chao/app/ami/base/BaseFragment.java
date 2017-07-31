package chao.app.ami.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public class BaseFragment extends Fragment implements IBaseFragment{
    private BaseFragmentHelper mHelper = new BaseFragmentHelper(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHelper.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return mHelper.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHelper.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHelper.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHelper.onDetach();
    }

    @Override
    public <T extends View> T findViewById(int resId) {
        return mHelper.findViewById(resId);
    }

}
