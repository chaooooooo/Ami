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
public abstract class AmiPlugin<PluginFragment extends Fragment, Settings extends AmiSettings, Component extends AmiGeneralComponent> implements IPlugin {

    protected Context mAppContext;

    protected PluginFragment mFragment;

    private Component mComponent;

    private Activity mActivity;

    private Settings mSetting;

    private AmiContentView contentView;

    public AmiPlugin() {
        mAppContext = Ami.getApp();
    }

    public CharSequence getTitle(int titleId) {
        return mAppContext.getString(titleId);
    }


    @Override
    public PluginFragment getFragment() {
        return mFragment;
    }

    @Override
    public PluginFragment newFragment() {
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

    public abstract Settings createSettings();

    public Settings getSettings() {
        if (mSetting == null) {
            mSetting = createSettings();
        }
        return mSetting;
    }

    @CallSuper
    public void onActivityChanged(FragmentActivity activity) {
        mActivity = activity;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public Component getComponent() {
        if (mComponent == null) {
            mComponent = createComponent();
        }
        return mComponent;
    }

    protected abstract PluginFragment createFragment();

    public abstract Component createComponent();

    public void destroyFragment() {
        mFragment = null;
    }

}

