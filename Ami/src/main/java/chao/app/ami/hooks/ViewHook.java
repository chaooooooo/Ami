package chao.app.ami.hooks;

import android.view.View;

import java.lang.reflect.Field;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class ViewHook {

    private static Class<?> View_ListenerInfo;

    private static Field View_mListenerInfo;
    private static Field View_ListenerInfo_mOnClickListener;
    private static Field View_ListenerInfo_mOnLongClickListener;
    private static Field View_ListenerInfo_mOnTouchListener;

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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static Object getListenerInfo(View view) throws IllegalAccessException {
        return View_mListenerInfo.get(view);
    }

    public static View.OnClickListener getOnClickListener(View view) {
        try {
            Object listenerInfo = getListenerInfo(view);
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
}
