package chao.app.ami.plugin;

import androidx.fragment.app.Fragment;
import chao.app.ami.base.AmiContentView;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public interface IPlugin {

    Fragment getFragment();

    Fragment newFragment();


    CharSequence getTitle();

    Object getManager();

    void onCreate();

    void onDestroy();

    void onBindView(AmiContentView contentView);
}
