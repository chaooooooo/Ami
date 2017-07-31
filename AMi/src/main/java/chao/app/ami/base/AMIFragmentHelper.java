package chao.app.ami.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chao.app.ami.AMi;
import chao.app.ami.AMiException;
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
            throw new AMiException("FragmentHelper should have a fragment.");
        }
        mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {
        lifecycle("onAttach()", AMi.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lifecycle("onCreateView()", AMi.LIFECYCLE_LEVEL_SIMPLE);
        int layoutId = getLayoutFromAnnotation();
        if (layoutId == View.NO_ID) {
            return null;
        }
        mLayout = inflater.inflate(layoutId,container,false);
        return mLayout;
    }

    @Override
    public void onStart() {
        lifecycle("onStart()", AMi.LIFECYCLE_LEVEL_SIMPLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        lifecycle("onActivityCreated()", AMi.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onResume() {
        lifecycle("onResume()", AMi.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onPause() {
        lifecycle("onPause()", AMi.LIFECYCLE_LEVEL_FULL);
    }

    @Override
    public void onStop() {
        lifecycle("onStop()", AMi.LIFECYCLE_LEVEL_SIMPLE);
    }

    @Override
    public void onDestroyView() {
        lifecycle("onDestroyView()", AMi.LIFECYCLE_LEVEL_SIMPLE);
    }

    @Override
    public void onDestroy() {
        lifecycle("onDestroy()", AMi.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onDetach() {
        lifecycle("onDetach()", AMi.LIFECYCLE_LEVEL_FULL);
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
    
    private void lifecycle(String log, int level) {
        AMi.lifecycle(TAG, mFragment.toString() + " -------> " + log, level);
    }
}
