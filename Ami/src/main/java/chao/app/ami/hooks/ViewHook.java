package chao.app.ami.hooks;

import android.annotation.SuppressLint;
import android.view.View;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chao.qin
 * @since 2017/8/8
 */
@SuppressLint("PrivateApi")
public class ViewHook {

    private static Class<?> View_ListenerInfo;

    private static Field View_mListenerInfo;
    private static Field View_ListenerInfo_mOnClickListener;
    private static Field View_ListenerInfo_mOnLongClickListener;
    private static Field View_ListenerInfo_mOnTouchListener;

    private static Method View_setFlags;

    static {
        try {
            View_mListenerInfo = View.class.getDeclaredField("mListenerInfo");
            View_mListenerInfo.setAccessible(true);

            View_ListenerInfo = View_mListenerInfo.getType();
            View_ListenerInfo_mOnClickListener = View_ListenerInfo.getDeclaredField("mOnClickListener");
            View_ListenerInfo_mOnClickListener.setAccessible(true);

            View_ListenerInfo_mOnTouchListener = View_ListenerInfo.getDeclaredField("mOnTouchListener");
            View_ListenerInfo_mOnTouchListener.setAccessible(true);

            View_ListenerInfo_mOnLongClickListener = View_ListenerInfo.getDeclaredField("mOnLongClickListener");
            View_ListenerInfo_mOnLongClickListener.setAccessible(true);

            View_setFlags = View.class.getDeclaredMethod("setFlags", int.class, int.class);
            View_setFlags.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Object getListenerInfo(View view) {
        try {
            return View_mListenerInfo.get(view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static View.OnClickListener getOnClickListener(View view) {
        try {
            Object listenerInfo = getListenerInfo(view);
            if (listenerInfo == null) {
                return null;
            }
            return (View.OnClickListener) View_ListenerInfo_mOnClickListener.get(listenerInfo);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static View.OnTouchListener getOnTouchListener(View view) {
        try {
            Object listenerInfo = getListenerInfo(view);
            if (listenerInfo == null) {
                return null;
            }

            return (View.OnTouchListener) View_ListenerInfo_mOnTouchListener.get(listenerInfo);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static View.OnLongClickListener getOnLongClickListener(View view) {
        try {
            Object listenerInfo = getListenerInfo(view);
            if (listenerInfo == null) {
                return null;
            }
            return (View.OnLongClickListener) View_ListenerInfo_mOnLongClickListener.get(listenerInfo);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFlag(View view, int flags, int mask) {
        try {
            View_setFlags.invoke(view, flags, mask);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
