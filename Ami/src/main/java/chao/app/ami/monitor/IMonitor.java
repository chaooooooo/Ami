package chao.app.ami.monitor;


import android.app.Application;
import android.os.Bundle;

public interface IMonitor {
    void onCreate(Application app);
    void onDestroy();
    void doMonitorAction(Bundle bundle);
}
