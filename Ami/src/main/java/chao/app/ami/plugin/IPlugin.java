package chao.app.ami.plugin;

import android.support.v4.app.Fragment;
import chao.app.ami.base.AmiContentView;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public interface IPlugin {

    Fragment getFragment();

    Fragment newFragment();

    AmiSettings getSettings();

    CharSequence getTitle();

    Object getManager();

    void onCreate();

    void onDestroy();

    void onBindView(AmiContentView contentView);
}
