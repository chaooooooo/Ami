package chao.app.ami.utils;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chao.qin
 * @since 2018/8/10
 */
@SuppressLint("all")
public class SystemPropertiesUtil {

    private static Class SystemProperties;

    private static Method get;

    private static Method getInt;

    private static Method getLong;

    private static Method getBoolean;

     static {
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            get = SystemProperties.getDeclaredMethod("get", String.class, String.class);
            getInt = SystemProperties.getDeclaredMethod("getInt", String.class, int.class);
            getLong = SystemProperties.getDeclaredMethod("getLong", String.class, long.class);
            getBoolean = SystemProperties.getDeclaredMethod("getBoolean", String.class, boolean.class);
            get.setAccessible(true);
            getInt.setAccessible(true);
            getBoolean.setAccessible(true);
            getLong.setAccessible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
     }

    public static String getString(String key, String def) {
        try {
            return (String) get.invoke(null, key, def);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static int getInt(String key, int def) {
        try {
            return (Integer) getInt.invoke(null, key, def);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static long getLong(String key, long def) {
        try {
            return (Long) getLong.invoke(null, key, def);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return (Boolean) getBoolean.invoke(null, key, def);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

}
