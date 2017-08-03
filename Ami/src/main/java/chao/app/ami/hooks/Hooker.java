package chao.app.ami.hooks;

import android.app.Activity;
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

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static Field FragmentController_FragmentHostCallback;
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static Field FragmentHostCallback_FragmentManagerImpl;


    //methods
    public static Method FragmentManagerImpl_moveToStateFIIIB;
    public static Method FragmentManagerImpl_moveToStateIIIB;


    //android 23
    static {
//        sClassLoader = getClass().getClassLoader();
        try {
            Activity_FragmentController = Activity.class.getDeclaredField("mFragments");
            Activity_FragmentController.setAccessible(true);
//            FragmentController_FragmentHostCallback = FragmentController.class.getDeclaredField("mHost");
//            FragmentController_FragmentHostCallback.setAccessible(true);
//            FragmentHostCallback_FragmentManagerImpl = FragmentHostCallback.class.getDeclaredField("mFragmentManager");
//            FragmentHostCallback_FragmentManagerImpl.setAccessible(true);

//            FragmentManagerImpl = FragmentHostCallback_FragmentManagerImpl.getType();
//
//            FragmentManagerImpl_moveToStateFIIIB = FragmentManagerImpl.getDeclaredMethod("moveToState", Fragment.class, int.class, int.class, int.class, boolean.class);//Fragment f, int newState, int transit, int transitionStyle,boolean keepActive
//            FragmentManagerImpl_moveToStateFIIIB.setAccessible(true);
//
//            FragmentManagerImpl_moveToStateIIIB = FragmentManagerImpl.getDeclaredMethod("moveToState", int.class, int.class, int.class, boolean.class);//int newState, int transit, int transitionStyle,boolean keepActive
//            FragmentManagerImpl_moveToStateIIIB.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


    }


}
