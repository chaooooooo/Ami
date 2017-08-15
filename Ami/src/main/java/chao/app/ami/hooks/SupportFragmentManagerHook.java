package chao.app.ami.hooks;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentHostCallback;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/3
 * <p>
 * <p>
 * todo 版本兼容性测试
 */

public class SupportFragmentManagerHook {

    private static Class<?> FragmentManager_FragmentManagerImpl;

    private static Field Activity_FragmentController; // Android M
    private static Field FragmentController_FragmentHost;   // Android M


    public static Field mFragmentManagerImpl;
    public static Field FragmentManager_mActive;


    static {
        try {
            Activity_FragmentController = FragmentActivity.class.getDeclaredField("mFragments");
            Activity_FragmentController.setAccessible(true);

            FragmentController_FragmentHost = FragmentController.class.getDeclaredField("mHost");
            FragmentController_FragmentHost.setAccessible(true);

            mFragmentManagerImpl = FragmentHostCallback.class.getDeclaredField("mFragmentManager");
            mFragmentManagerImpl.setAccessible(true);


            FragmentManager_FragmentManagerImpl = mFragmentManagerImpl.getType();

            FragmentManager_mActive = FragmentManager_FragmentManagerImpl.getDeclaredField("mActive");
            FragmentManager_mActive.setAccessible(true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Object getFragmentManagerImpl(FragmentActivity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Object controller = Activity_FragmentController.get(activity);
                Object host = FragmentController_FragmentHost.get(controller);
                return mFragmentManagerImpl.get(host);
            } else {
                return mFragmentManagerImpl.get(activity);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Fragment> getActiveFragments(FragmentActivity activity) {
        try {
            return (ArrayList<Fragment>) FragmentManager_mActive.get(getFragmentManagerImpl(activity));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
