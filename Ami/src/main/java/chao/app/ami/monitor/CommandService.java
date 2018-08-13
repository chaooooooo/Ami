package chao.app.ami.monitor;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import chao.app.ami.Ami;

/**
 * Created by qinchao on 2017/9/8.
 */

public class CommandService extends IntentService {

    private static final String ACTION_ADD_SERVER = "service.monitor.add";
    private static final String ACTION_REMOVE_SERVER = "service.monitor.remove";

    private static final String ACTION_MONITOR_COMMAND = "service.monitor.cmd";

    private MonitorManager mManager;

    public CommandService() {
        this("CommandService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CommandService(String name) {
        super(name);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mManager = MonitorManager.getInstance();
        mManager.init(Ami.getApp());
        for (AbsMonitor monitor: mManager.getMonitors()) {
            monitor.onCreate(getApplication());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (AbsMonitor monitor: mManager.getMonitors()) {
            monitor.onDestroy();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
       if (ACTION_MONITOR_COMMAND.equals(action)) {
            String name = intent.getStringExtra("name");
            if (TextUtils.isEmpty(name)) {
                return;
            }
            action = name;
        }
        Log.e("luqin", "monitors : " + mManager.getMonitors().toString());
        for (AbsMonitor monitor: mManager.getMonitors()) {
            String monitorAction = monitor.getAction();
            if (TextUtils.isEmpty(monitorAction)) {
                continue;
            }
            if (monitorAction.equals(action)) {
                monitor.doMonitorAction(bundle);
            }
        }
    }
}
