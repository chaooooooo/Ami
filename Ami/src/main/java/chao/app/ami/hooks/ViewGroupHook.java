package chao.app.ami.hooks;

import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class ViewGroupHook {

    public static Field ViewGroup_mOnHierarchyChangeListener;

    static {
        try {
            ViewGroup_mOnHierarchyChangeListener = ViewGroup.class.getDeclaredField("mOnHierarchyChangeListener");
            ViewGroup_mOnHierarchyChangeListener.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static ViewGroup.OnHierarchyChangeListener getOnHierarchyChangeListener(ViewGroup vg) {
        try {
            return (ViewGroup.OnHierarchyChangeListener) ViewGroup_mOnHierarchyChangeListener.get(vg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
