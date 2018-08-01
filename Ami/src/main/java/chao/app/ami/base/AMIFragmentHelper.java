package chao.app.ami.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chao.app.ami.Ami;
import chao.app.ami.AmiException;
import chao.app.ami.annotations.LayoutID;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public class AMIFragmentHelper implements IAMIFragment {

    private static final String TAG = AMIFragmentHelper.class.getSimpleName();


    private IAMIFragment mFragment;
    private View mLayout;

    AMIFragmentHelper(IAMIFragment fragment) {
        if (fragment == null) {
            throw new AmiException("FragmentHelper should have a fragment.");
        }
        mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {
        lifecycle("onAttach()", Ami.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lifecycle("onCreateView()", Ami.LIFECYCLE_LEVEL_SIMPLE);
        int layoutId = getLayoutID();
        if (layoutId == View.NO_ID) {
            return null;
        }
        mLayout = inflater.inflate(layoutId,container,false);
        setupView(mLayout);
        return mLayout;
    }

    public int getLayoutID() {
        int id = mFragment.getLayoutID();
        if (id == View.NO_ID) {
            id = getLayoutFromAnnotation();
        }
        return id;
    }

    @Override
    public void onStart() {
        lifecycle("onStart()", Ami.LIFECYCLE_LEVEL_SIMPLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        lifecycle("onActivityCreated()", Ami.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onResume() {
        lifecycle("onResume()", Ami.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onPause() {
        lifecycle("onPause()", Ami.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onStop() {
        lifecycle("onStop()", Ami.LIFECYCLE_LEVEL_SIMPLE);
    }

    @Override
    public void onDestroyView() {
        lifecycle("onDestroyView()", Ami.LIFECYCLE_LEVEL_SIMPLE);
    }

    @Override
    public void onDestroy() {
        lifecycle("onDestroy()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onDetach() {
        lifecycle("onDetach()", Ami.LIFECYCLE_LEVEL_FULL);
    }

    private int getLayoutFromAnnotation() {
        LayoutID layoutID = mFragment.getClass().getAnnotation(LayoutID.class);
        if (layoutID == null) {
            return View.NO_ID;
        }
        return layoutID.value();
    }

    public <T extends View> T findView(int resId) {
        return (T) mLayout.findViewById(resId);
    }

    @Override
    public void setupView(View layout) {
        mFragment.setupView(layout);
    }

    private void lifecycle(String log, int level) {
        Ami.lifecycle(TAG, mFragment.toString() + " -------> " + log, level);
    }
}
