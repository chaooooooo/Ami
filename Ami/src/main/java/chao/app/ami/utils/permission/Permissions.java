package chao.app.ami.utils.permission;

import android.Manifest;

/**
 * Created by LuckyCrystal on 2017/1/25.
 */

public interface Permissions {
    /**
     * 权限申请CODE
     */
    int PERMISSION_REQUEST_CODE = 400;

    int PERMISSION_SETTING_CODE = 401;

    String[] PERMISSIONS_CALENDAR = new String[] {
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    };
    String[] PERMISSIONS_CALL_LOG = new String[] {
        "android.permission.READ_CALL_LOG",
        "android.permission.WRITE_CALL_LOG"
    };

    String[] PERMISSIONS_CAMERA = new String[]{
        Manifest.permission.CAMERA
    };

    String[] PERMISSIONS_CONTACTS = new String[]{
        Manifest.permission.READ_CONTACTS
    };

    String[] PERMISSIONS_LOCATION = new String[]{
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.CONTROL_LOCATION_UPDATES,
        Manifest.permission.INSTALL_LOCATION_PROVIDER,
        "android.permission.LOCATION_HARDWARE"
    };

    String[] PERMISSIONS_PHONE_STATE = new String[]{
            Manifest.permission.READ_PHONE_STATE,
        "android.permission.ANSWER_PHONE_CALLS",
        Manifest.permission.READ_PHONE_STATE,
        "android.permission.READ_PHONE_NUMBERS",
        Manifest.permission.CALL_PHONE,
        Manifest.permission.MODIFY_PHONE_STATE
    };

    String[] PERMISSIONS_SENSORS = new String[] {
        "android.permission.BODY_SENSORS"
    };

    String[] PERMISSIONS_EXTERNAL_STORAGE = new String[]{
            "android.permission.READ_EXTERNAL_STORAGE",
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String[] PERMISSIONS_RECORD_AUDIO = new String[]{
            Manifest.permission.RECORD_AUDIO
    };
    String[] PERMISSIONS_SMS = new String[]{
            Manifest.permission.READ_SMS,
        Manifest.permission.BROADCAST_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.SEND_SMS
    };

}
