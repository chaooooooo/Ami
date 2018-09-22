package chao.app.ami.plugin;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import chao.app.ami.Ami;
import chao.app.ami.base.AmiContentView;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public abstract class AmiPlugin implements IPlugin {

    protected Context mAppContext;

    protected Fragment mFragment;

    private Activity mActivity;

    private AmiContentView contentView;

    private int mFragmentIndex;

    public AmiPlugin() {
        mAppContext = Ami.getApp();
    }

    public CharSequence getTitle(int titleId) {
        return mAppContext.getString(titleId);
    }


    @Override
    public Fragment getFragment() {
        return mFragment;
    }

    @Override
    public Fragment newFragment() {
        mFragment = createFragment();
        return mFragment;
    }

    public AmiContentView getContentView() {
        return contentView;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {

    }

    @CallSuper
    public void onActivityChanged(FragmentActivity activity) {
        mActivity = activity;
    }

    public Activity getActivity() {
        return mActivity;
    }

    protected abstract Fragment createFragment();

    public abstract AmiGeneralComponent getComponent();

    public void destroyFragment() {
        mFragment = null;
    }

    public void setFragmentIndex(int mIndex) {
        this.mFragmentIndex = mIndex;
    }
}

