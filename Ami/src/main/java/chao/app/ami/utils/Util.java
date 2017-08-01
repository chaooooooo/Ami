package chao.app.ami.utils;

import android.app.Application;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author chao.qin
 * @since 2017/7/26
 */

public class Util {

    public static boolean isHostAppDebugMode(Application app) {
        String packageName = app.getPackageName();
        try {
            Class<?> buildConfig = Class.forName(packageName + ".BuildConfig");
            Field debugField = buildConfig.getDeclaredField("DEBUG");
            return (boolean) debugField.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <T extends Collection> String toString(T collection) {
        return null;
    }
}
