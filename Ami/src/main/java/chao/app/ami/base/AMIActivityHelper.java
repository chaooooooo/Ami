package chao.app.ami.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.content.res.Resources;

import chao.app.ami.Ami;
import chao.app.ami.annotations.LayoutID;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public class AMIActivityHelper implements IAMIActivity {

    private static final String TAG = AMIActivityHelper.class.getSimpleName();

    private Activity mActivity;


    public AMIActivityHelper(AMIActivity activity) {
        mActivity = activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
        int layoutId = getLayoutFromAnnotation();
        if (layoutId != View.NO_ID) {
            mActivity.setContentView(layoutId);
        }
    }

    public Resources getResource() {
        return mActivity.getResources();
    }

    public String getString(int resId) {
        return mActivity.getString(resId);
    }

    public String getString(int resId,Object... args) {
        return mActivity.getString(resId, args);
    }

    @Override
    public void onStart() {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onResume() {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onPause() {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onStop() {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onDestroy() {
        lifecycle("onCreate()", Ami.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public <T extends View> T findView(int resId) {
        return (T) mActivity.findViewById(resId);
    }

    private int getLayoutFromAnnotation() {
        LayoutID layoutID = mActivity.getClass().getAnnotation(LayoutID.class);
        if (layoutID == null) {
            return View.NO_ID;
        }
        return layoutID.value();
    }

    private void lifecycle(String log, int level) {
        Ami.lifecycle(TAG, mActivity.toString() + " -------> " + log, level);
    }
}
