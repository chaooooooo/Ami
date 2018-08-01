package chao.app.ami.monitor;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import chao.app.ami.monitor.annotations.Action;


/**
 * Created by qinchao on 2017/9/8.
 */

@Action("service.monitor.gc")
public class GCMonitor extends AbsMonitor {

    public GCMonitor(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Application app) {
        super.onCreate(app);
    }

    @Override
    public void doMonitorAction(Bundle bundle) {
        Log.e("luqin", "gc");
        System.gc();
    }
}
