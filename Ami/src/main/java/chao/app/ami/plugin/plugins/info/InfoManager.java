package chao.app.ami.plugin.plugins.info;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.command.beans.Screen;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.MovementLayout;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoManager implements AmiSettings.OnSettingsChangeListener {

    private TextView appInfoView;

    private TextView displayView;

    private InfoSettings settings;

    private String screenInfo;

    public InfoManager(@NonNull AmiContentView contentView, final InfoSettings settings) {
        this.settings  = settings;
        settings.setSettingsChangeListener(this);


        MovementLayout movementLayout = contentView.getMovementLayout();

        //appInfo
        appInfoView = (TextView) contentView.findViewById(R.id.ami_content_app_info);
        movementLayout.addView(appInfoView);


        displayView = (TextView) contentView.findViewById(R.id.ami_content_app_display);
        movementLayout.addView(displayView);
        updateVisible();
    }

    private void updateVisible() {
        if (settings.isShowAppInfo()){
            appInfoView.setVisibility(View.VISIBLE);
        } else {
            appInfoView.setVisibility(View.GONE);
        }

        if (settings.isShowDisplayMetrics()) {
            displayView.setVisibility(View.VISIBLE);
        } else {
            displayView.setVisibility(View.GONE);
        }
    }

    void setupManager(Activity activity) {
        appInfoView.setText(getAppInfo(activity));
        if (screenInfo == null) {
            screenInfo = getDisplayInfo(activity);
        }
        displayView.setText(screenInfo);
    }

    private String getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Screen screen = new Screen(dm);
        return screen.toString();
    }

    private String getAppInfo(Activity activity) {
        StringBuilder buffer = new StringBuilder();
        try {
            PackageInfo packageInfo = activity.getApplicationContext()
                .getPackageManager()
                .getPackageInfo(activity.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            String applicationId = packageInfo.packageName;
            String activityName = activity.getComponentName().getClassName();
            String appName = activity.getString(packageInfo.applicationInfo.labelRes);
            buffer.append("应用名称: ").append(appName).append("\n")
                .append("版本号: ").append(versionName).append("(").append(versionCode).append(")").append("\n")
                .append("包名: ").append(applicationId).append("\n")
                .append("activity: ").append(activityName).append("\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Override
    public void onSettingsChanged(String key, Object value) {
        updateVisible();
    }
}
