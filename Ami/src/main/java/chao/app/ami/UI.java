package chao.app.ami;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import chao.app.ami.utils.permission.PermissionHelper;
import chao.app.ami.utils.permission.PermissionListener;


/**
 * @author chao.qin
 * @since 2017/3/23
 */

public class UI {

    public static void show(Context context, Class clazz) {
        show(context,clazz,null,0);
    }

    public static void show(Context context, Class clazz, Bundle bundle) {
        show(context,clazz,bundle,0);
    }

    public static void show(Context context, Class clazz, Bundle bundle, int flags) {
        showInner(context, clazz, bundle, flags);
    }

    /**
     *
     * @param permissions 权限
     */
    public static void show(final Context context, final Class clazz, final Bundle bundle, final int flags, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            showInner(context, clazz, bundle, flags);
            return;
        }
        PermissionHelper.requestPermissions(context, permissions, new PermissionListener() {
            @Override
            public void onPassed() {
                showInner(context, clazz, bundle, flags);
            }

            @Override
            public boolean onDenied(boolean neverAsk) {
                Toast.makeText(context, "当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限", Toast.LENGTH_LONG).show();
                return super.onDenied(neverAsk);
            }
        });

    }

    private static void showInner(Context context, Class clazz, Bundle bundle, int flags) {
        if (android.app.Fragment.class.isAssignableFrom(clazz)) {
            showAppFragment(context,clazz,bundle,flags);
        } else if (Fragment.class.isAssignableFrom(clazz)) {
            showSupportFragment(context,clazz,bundle,flags);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            showActivity(context,clazz,bundle,flags);
        }
    }

    private static void showAppFragment(Context context, Class fragment, Bundle bundle, int flags) {
        Intent intent = FragmentContainer.buildContainerIntent(context, fragment);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!(context instanceof Activity)) {
            flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
        }
        intent.addFlags(flags);
        context.startActivity(intent);
    }

    private static void showSupportFragment(Context context, Class fragment,Bundle bundle, int flags) {
        Intent intent = SupportFragmentContainer.buildContainerIntent(context, fragment);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!(context instanceof Activity)) {
            flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
        }
        intent.addFlags(flags);
        context.startActivity(intent);
    }
    private static void showActivity(Context context, Class targetActivity,Bundle bundle, int flags) {
        Intent intent = new Intent(context,targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!(context instanceof Activity)) {
            flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
        }
        intent.addFlags(flags);
        context.startActivity(intent);
    }

    public static XmlResourceParser getXml(int resId) {
        return getResource().getXml(resId);
    }

    public static int getColor(int resId) {
        return ResourcesCompat.getColor(getResource(), resId, getTheme());
    }

    public static String getString(int resId, Object... args) {
        return getResource().getString(resId, args);
    }

    public static Drawable getDrawable(int drawableId) {
        return ResourcesCompat.getDrawable(getResource(), drawableId, getTheme());
    }

    public static Resources.Theme getTheme() {
        return getApp().getTheme();
    }

    public static Resources getResource() {
        return getApp().getResources();
    }

    public static Application getApp() {
        return Ami.getApp();
    }


}
