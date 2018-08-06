package chao.app.ami.hooks;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentHostCallback;
import android.util.SparseArray;

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
            try {
                mFragmentManagerImpl = Activity.class.getDeclaredField("mFragments");
                mFragmentManagerImpl.setAccessible(true);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static Object getFragmentManagerImpl(FragmentActivity activity) {
        try {
            try {
                Object controller = Activity_FragmentController.get(activity);
                Object host = FragmentController_FragmentHost.get(controller);
                return mFragmentManagerImpl.get(host);
            } catch (Throwable e) {
                return mFragmentManagerImpl.get(activity);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Fragment> getActiveFragments(FragmentActivity activity) {
        try {
            Object active = FragmentManager_mActive.get(getFragmentManagerImpl(activity));
            if (active instanceof ArrayList) {
                return (ArrayList<Fragment>) active;
            }
            if (active instanceof SparseArray) {
                ArrayList<Fragment> resultList = new ArrayList<>();
                SparseArray<Fragment> sparseActive = (SparseArray<Fragment>) active;
                for (int i=0; i < sparseActive.size() ; i++) {
                    resultList.add(sparseActive.valueAt(i));
                }
                return resultList;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
