package chao.app.ami.monitor;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import chao.app.ami.monitor.annotations.Action;


@Action("service.monitor.dump")
public class DumpMonitor extends AbsMonitor {

    public DumpMonitor(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Application app) {

    }

    @Override
    public void doMonitorAction(Bundle bundle) {
        ThreadMonitor.instance().dumpThreadInfo();
    }
}
