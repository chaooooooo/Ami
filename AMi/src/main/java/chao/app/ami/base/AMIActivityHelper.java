package chao.app.ami.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import chao.app.ami.AMi;
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
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
        int layoutId = getLayoutFromAnnotation();
        if (layoutId != View.NO_ID) {
            mActivity.setContentView(layoutId);
        }
    }

    @Override
    public void onStart() {
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onResume() {
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onPause() {
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onStop() {
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
    }

    @Override
    public void onDestroy() {
        lifecycle("onCreate()", AMi.LIFECYCLE_LEVEL_CREATE);
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
        AMi.lifecycle(TAG, mActivity.toString() + " -------> " + log, level);
    }
}
