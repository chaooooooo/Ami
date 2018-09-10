package chao.app.ami.plugin;

import android.support.v4.app.Fragment;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public interface AmiPlugin {

    Fragment getFragment();

    Fragment newFragment();

    CharSequence getTitle();

    void onCreate();

    void onDestroy();

    AmiSettings getSettings();
}
