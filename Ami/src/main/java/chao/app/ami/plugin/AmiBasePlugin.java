package chao.app.ami.plugin;

import android.content.Context;
import android.support.v4.app.Fragment;
import chao.app.ami.Ami;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public abstract class AmiBasePlugin implements AmiPlugin {

    protected Context mContext;

    private Fragment mFragment;

    public AmiBasePlugin() {
        mContext = Ami.getApp();
    }

    public CharSequence getTitle(int titleId) {
        return mContext.getString(titleId);
    }


    @Override
    public Fragment getFragment() {
        if (mFragment == null) {
            return newFragment();
        }
        return mFragment;
    }

    @Override
    public Fragment newFragment() {
        mFragment = createFragment();
        return mFragment;
    }

    protected abstract Fragment createFragment();

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}

