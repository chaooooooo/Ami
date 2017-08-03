package chao.app.ami.hooks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import chao.app.ami.launcher.drawer.DrawerManager;

/**
 * @author chao.qin
 * @since 2017/8/3
 */

public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks{



    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        DrawerManager.get().injectInput(f.getActivity());
    }
}
