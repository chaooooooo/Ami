package android.app;

import java.lang.reflect.InvocationTargetException;

import chao.app.ami.Ami;
import chao.app.ami.hooks.Hooker;


/**
 * @author chao.qin
 * @since 2017/8/6
 */

public class FragmentManagerImplHook {

    private FragmentManager mfm;

    public FragmentManagerImplHook(FragmentManager fm) {
        mfm = fm;
    }

    public void dispatchCreate() {
        Ami.log("impl hook dispatch create()");
        try {
            Hooker.FragmentManagerImpl_dispatchCreate.invoke(mfm);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    void moveToState(Fragment f, int newState, int transit, int transitionStyle, boolean keepActive) {

    }

    void moveToState(int newState, int transit, int transitStyle, boolean always) {

    }

    void moveToState(int newState, boolean always) {

    }

    void moveToState(Fragment fragment) {

    }
}
