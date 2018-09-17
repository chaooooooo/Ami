package chao.app.debugtools.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;
import java.util.List;

/**
 * @author qinchao
 * @since 2018/9/14
 */
public class PackageUtils {

    public static List<ResolveInfo> queryActivities(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        return packageManager.queryIntentActivities(mainIntent, 0);

    }

    public static void openApp(Context context, String packname) {
        PackageManager packageManager = context.getPackageManager();
        if (checkPackInfo(context, packname)) {
            Intent intent = packageManager.getLaunchIntentForPackage(packname);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "没有安装" + packname, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

}
