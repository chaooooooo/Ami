package chao.app.ami.hooks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentController;
import android.app.FragmentHostCallback;
import android.app.FragmentManager;
import android.app.FragmentManagerImplHook;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author chao.qin
 * @since 2017/8/3
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class Hooker {

    public static Object mFragmentController;
    public static Object mFragmentManagerImpl;

    public static ClassLoader sClassLoader;

    //classes
    public static Class FragmentManagerImpl;

    //fields
    public static Field Activity_FragmentController; //

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Field FragmentController_FragmentHostCallback;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Field FragmentHostCallback_FragmentManagerImpl;


    //methods
    public static Method FragmentManagerImpl_moveToStateFIIIB;
    public static Method FragmentManagerImpl_moveToStateIIIB;

    public static Method FragmentManagerImpl_moveToStateF;
    public static Method FragmentManagerImpl_moveToStateIB;

    public static Method FragmentManagerImpl_dispatchCreate;


    //android 23
    static {
//        sClassLoader = getClass().getClassLoader();
        try {
            Activity_FragmentController = Activity.class.getDeclaredField("mFragments");
            Activity_FragmentController.setAccessible(true);



            FragmentController_FragmentHostCallback = FragmentController.class.getDeclaredField("mHost");
            FragmentController_FragmentHostCallback.setAccessible(true);
            FragmentHostCallback_FragmentManagerImpl = FragmentHostCallback.class.getDeclaredField("mFragmentManager");
            FragmentHostCallback_FragmentManagerImpl.setAccessible(true);

            FragmentManagerImpl = FragmentHostCallback_FragmentManagerImpl.getType();

            FragmentManagerImpl_moveToStateFIIIB = FragmentManagerImpl.getDeclaredMethod("moveToState", Fragment.class, int.class, int.class, int.class, boolean.class);//Fragment f, int newState, int transit, int transitionStyle,boolean keepActive
            FragmentManagerImpl_moveToStateFIIIB.setAccessible(true);

            FragmentManagerImpl_moveToStateIB = FragmentManagerImpl.getDeclaredMethod("moveToState", int.class, boolean.class);//int newState, int transit, int transitionStyle,boolean keepActive
            FragmentManagerImpl_moveToStateIB.setAccessible(true);

            FragmentManagerImpl_moveToStateF = FragmentManagerImpl.getDeclaredMethod("moveToState", Fragment.class);//int newState, int transit, int transitionStyle,boolean keepActive
            FragmentManagerImpl_moveToStateF.setAccessible(true);

            FragmentManagerImpl_dispatchCreate = Hooker.FragmentManagerImpl.getDeclaredMethod("dispatchCreate");
            FragmentManagerImpl_dispatchCreate.setAccessible(true);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }


    public static void hookFragmentManager(FragmentManager fm) {
        try {
            FragmentManagerImplHook implHook = new FragmentManagerImplHook(fm);
            Method target = FragmentManagerImplHook.class.getDeclaredMethod("dispatchCreate");
            Method method = Hooker.FragmentManagerImpl_dispatchCreate;
//            AndFix.addReplaceMethod(method, target);

//            Method moveToStateFIIIB = Hooker.FragmentManagerImpl_moveToStateFIIIB;
//            Method moveToStateFIIIBTarget = FragmentManagerImplHook.class.getDeclaredMethod("moveToState", Fragment.class, int.class, int.class, int.class, boolean.class);
//            AndFix.addReplaceMethod(moveToStateFIIIB, moveToStateFIIIBTarget);
//
//            Method moveToStateIIIB = Hooker.FragmentManagerImpl_moveToStateIIIB;
//            Method moveToStateIIIBTarget = FragmentManagerImplHook.class.getDeclaredMethod("moveToState", int.class, int.class, int.class, boolean.class);
//            AndFix.addReplaceMethod(moveToStateIIIB, moveToStateIIIBTarget);


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
