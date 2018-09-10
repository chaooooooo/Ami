package chao.app.ami.plugin.plugins.info;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.fps.FPSManager;
import chao.app.debug.R;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class InfoManager {

    private TextView fpsView;

    private TextView appInfoView;

    private InfoSettings settings;

    public InfoManager(@NonNull AmiContentView contentView) {
        settings = new InfoSettings(this);
        //fps
        fpsView = (TextView) contentView.findViewById(R.id.ami_content_fps);
        FPSManager fpsManager = new FPSManager(new FPSManager.OnFPSUpdateListener() {
            @Override
            public void onFpsUpdate(int fps) {
                String text = "fps: " + fps;
                fpsView.setText(text);
            }
        });
        fpsManager.start();

        //appInfo
        appInfoView = (TextView) contentView.findViewById(R.id.ami_content_app_info);
        updateVisible();
    }

    public void  updateVisible() {
        if (settings.isShowAppInfo()){
            appInfoView.setVisibility(View.VISIBLE);
        } else {
            appInfoView.setVisibility(View.GONE);
        }

        if (settings.isShowFPS()) {
            fpsView.setVisibility(View.VISIBLE);
        } else {
            fpsView.setVisibility(View.GONE);
        }
    }

    public void setupManager(Activity activity) {
        appInfoView.setText(getAppInfo(activity));
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

    public InfoSettings getSettings() {
        return settings;
    }
}
