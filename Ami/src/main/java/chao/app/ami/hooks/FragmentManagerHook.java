package chao.app.ami.hooks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentController;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import chao.app.ami.Ami;


/**
 * @author chao.qin
 * @since 2017/8/3
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FragmentManagerHook extends FragmentManager {

    private FragmentManager mBase;


    public FragmentManagerHook(FragmentManager fragmentManager) {
        mBase = fragmentManager;
    }

    @Override
    public FragmentTransaction beginTransaction() {
        return mBase.beginTransaction();
    }

    @Override
    public boolean executePendingTransactions() {
        return mBase.executePendingTransactions();
    }

    @Override
    public Fragment findFragmentById(@IdRes int id) {
        return mBase.findFragmentById(id);
    }

    @Override
    public Fragment findFragmentByTag(String tag) {
        return mBase.findFragmentByTag(tag);
    }

    @Override
    public void popBackStack() {
        mBase.popBackStack();
    }

    @Override
    public boolean popBackStackImmediate() {
        return mBase.popBackStackImmediate();
    }

    @Override
    public void popBackStack(String name, int flags) {
        mBase.popBackStack();
    }

    @Override
    public boolean popBackStackImmediate(String name, int flags) {
        return mBase.popBackStackImmediate(name, flags);
    }

    @Override
    public void popBackStack(int id, int flags) {
        mBase.popBackStack(id, flags);
    }

    @Override
    public boolean popBackStackImmediate(int id, int flags) {
        return mBase.popBackStackImmediate(id, flags);
    }

    @Override
    public int getBackStackEntryCount() {
        return mBase.getBackStackEntryCount();
    }

    @Override
    public BackStackEntry getBackStackEntryAt(int index) {
        return mBase.getBackStackEntryAt(index);
    }

    @Override
    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        mBase.addOnBackStackChangedListener(listener);
    }

    @Override
    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        mBase.addOnBackStackChangedListener(listener);
    }

    @Override
    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        mBase.putFragment(bundle, key, fragment);
    }

    @Override
    public Fragment getFragment(Bundle bundle, String key) {
        return mBase.getFragment(bundle, key);
    }

    @Override
    public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
        return mBase.saveFragmentInstanceState(f);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean isDestroyed() {
        return mBase.isDestroyed();
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        mBase.dump(prefix, fd, writer, args);
    }

    void moveToState(Fragment f, int newState, int transit, int transitionStyle, boolean keepActive) {
        try {
            Hooker.FragmentManagerImpl_moveToStateFIIIB.invoke(this, f,newState,transit, transitionStyle, keepActive);
            Ami.log("moveToStateFIIIB invoked");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    void moveToState(int newState, int transit, int transitStyle, boolean always) {
        try {
            Hooker.FragmentManagerImpl_moveToStateFIIIB.invoke(this,newState,transit, transitStyle, always);
            Ami.log("moveToStateIIIB invoked");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void hook(Activity activity) {
        FragmentController fragmentController = null;
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentManagerHook managerHook = new FragmentManagerHook(fragmentManager);
        try {
            fragmentController = (FragmentController) Hooker.Activity_FragmentController.get(activity);
//            FragmentHostCallback host = (FragmentHostCallback) Hooker.FragmentController_FragmentHostCallback.get(fragmentController);
//            Hooker.FragmentHostCallback_FragmentManagerImpl.set(host, managerHook);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
