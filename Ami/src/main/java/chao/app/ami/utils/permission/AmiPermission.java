package chao.app.ami.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import chao.app.ami.Ami;
import chao.app.ami.UI;
import chao.app.ami.utils.Util;
import java.util.Arrays;


/**
 * Created by LuckyCrystal on 2017/1/25.
 */

public class AmiPermission {

    private Builder builder;

    private static AmiPermission axdPermission;

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static Builder builder(Activity mActivity) {
        return new Builder(mActivity);
    }

    public static class Builder {
        private String[] permissions;
        private Context context;
        private PermissionListener permissionListener;
        private String tipContent=null;
        private int code = Permissions.PERMISSION_REQUEST_CODE;

        private Builder(Context context) {
            this.context = context;
        }


        public Builder setCode(int code) {
            this.code = code;
            return this;
        }

        public Builder setTipContent(String tipContent) {
            this.tipContent = tipContent;
            return this;
        }

        public Builder callBack(PermissionListener permissionListener) {

            this.permissionListener = permissionListener;
            return this;
        }

        public Builder setPermissions(String[] permissions) {
            Ami.log("permissionListener--" + "setPermissions" + Arrays.toString(permissions));
            this.permissions = permissions;
            return this;
        }

        public void request() {
            AmiPermission axdPermission = new AmiPermission();
            axdPermission.builder = this;
            AmiPermission.axdPermission = axdPermission;
            if (permissions != null && permissions.length != 0) {
                axdPermission.requestPermission();
            }
        }
    }

    public PermissionListener getPermissionListener() {
        if (builder == null) {
            return null;
        }
        return builder.permissionListener;
    }

    private void requestPermission() {
        if (PermissionUtil.hasSelfPermissions(builder.context, builder.permissions) || !Util.isOverMarshmallow()) {
            builder.permissionListener.onPassed();
        } else {
            String[] unPassPermissions = PermissionUtil.cutPassPermissions(builder.context, builder.permissions);
            if (unPassPermissions.length == 0) {
                builder.permissionListener.onPassed();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putStringArray("permission", unPassPermissions);
            bundle.putInt("code", builder.code);
            UI.show(builder.context, PermissionActivity.class, bundle);
        }
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode == builder.code && PermissionUtil.verifyPermissions(grantResults))
                || PermissionUtil.hasSelfPermissions(builder.context, builder.permissions)) {

            builder.permissionListener.onPassed();
        } else {
            boolean neverAsk = !PermissionUtil.shouldShowRequestPermissionRationale(activity, permissions);
            builder.permissionListener.onDenied(neverAsk);

        }
    }

    /**
     *从设置页面返回后
     */
    public void onSettingResult(@NonNull AmiPermission axdPermission) {
        axdPermission.requestPermission();
    }

    public void destroy() {
        builder = null;
    }

    public static class PermissionActivity extends Activity {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent data = getIntent();
            if (data == null) {
                return;
            }
            String[] permissions = data.getStringArrayExtra("permission");
            int code = data.getIntExtra("code", 0);

            ActivityCompat.requestPermissions(this, permissions, code);

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Ami.log();
            if (requestCode == Permissions.PERMISSION_SETTING_CODE) {
                onSettingResult(requestCode);
            }
        }


        protected void onSettingResult(int requestCode) {
            Ami.log("permission--" + "setting" + requestCode);
            if (axdPermission != null) {
                axdPermission.onSettingResult(axdPermission);
            }
        }


        /**
         * 权限请求结果
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            Ami.log();
            if (axdPermission != null && axdPermission.getPermissionListener() != null) {
                axdPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
            }
            finish();
        }


        @Override
        public void onDestroy() {
            if (axdPermission != null) {
                axdPermission.destroy();
                axdPermission = null;
            }
            super.onDestroy();
        }
    }
}
