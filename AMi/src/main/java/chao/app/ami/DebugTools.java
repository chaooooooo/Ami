package chao.app.ami;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * @author chao.qin
 * @since 2017/3/23
 */

public class DebugTools {

    public static void show(Context context, Class clazz) {
        show(context,clazz,null,0);
    }

    public static void show(Context context, Class clazz, Bundle bundle) {
        show(context,clazz,bundle,0);
    }

    public static void show(Context context, Class clazz, Bundle bundle, int flags) {
        if (android.app.Fragment.class.isAssignableFrom(clazz)) {
            showAppFragment(context,clazz,bundle,flags);
        } else if (android.support.v4.app.Fragment.class.isAssignableFrom(clazz)) {
            showSupportFragment(context,clazz,bundle,flags);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            showActivity(context,clazz,bundle,flags);
        }
    }

    private static void showAppFragment(Context context, Class fragment,Bundle bundle, int flags) {
        Intent intent = DebugFragmentContainer.buildContainerIntent(context, fragment);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        context.startActivity(intent);
    }

    private static void showSupportFragment(Context context, Class fragment,Bundle bundle, int flags) {
        Intent intent = DebugSupportFragmentContainer.buildContainerIntent(context, fragment);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        context.startActivity(intent);
    }
    private static void showActivity(Context context, Class targetActivity,Bundle bundle, int flags) {
        Intent intent = new Intent(context,targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(flags);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
