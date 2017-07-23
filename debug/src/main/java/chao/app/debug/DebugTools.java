package chao.app.debug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;



/**
 * @author chao.qin
 * @since 2017/3/23
 */

public class DebugTools {


    public static void show(Context context, Class clazz) {

        if (android.app.Fragment.class.isAssignableFrom(clazz)) {
            showAppFragment(context,clazz);
        } else if (android.support.v4.app.Fragment.class.isAssignableFrom(clazz)) {
            showSupportFragment(context,clazz);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            showActivity(context,clazz);
        }
    }

    public static void show(Context context, Class clazz, Bundle bundle) {
        if (android.app.Fragment.class.isAssignableFrom(clazz)) {
            showAppFragment(context,clazz,bundle);
        } else if (android.support.v4.app.Fragment.class.isAssignableFrom(clazz)) {
            showSupportFragment(context,clazz,bundle);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            showActivity(context,clazz,bundle);
        }
    }

    private static void showAppFragment(Context context, Class fragment,Bundle bundle) {
        Intent intent = DebugFragmentContainer.buildContainerIntent(context, fragment);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    private static void showSupportFragment(Context context, Class fragment,Bundle bundle) {
        Intent intent = DebugSupportFragmentContainer.buildContainerIntent(context, fragment);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    private static void showActivity(Context context, Class targetActivity,Bundle bundle) {
        Intent intent = new Intent(context,targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static void showAppFragment(Context context, Class fragment) {
        Intent intent = DebugFragmentContainer.buildContainerIntent(context, fragment);
        context.startActivity(intent);
    }

    private static void showSupportFragment(Context context, Class fragment) {
        Intent intent = DebugSupportFragmentContainer.buildContainerIntent(context, fragment);
        context.startActivity(intent);
    }
    private static void showActivity(Context context, Class targetActivity) {
        Intent intent = new Intent(context,targetActivity);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
