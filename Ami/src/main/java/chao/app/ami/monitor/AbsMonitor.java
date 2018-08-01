package chao.app.ami.monitor;

import android.app.Application;
import android.content.Context;

import chao.app.ami.monitor.annotations.Action;


/**
 * Created by qinchao on 2017/9/8.
 */

public abstract class AbsMonitor implements IMonitor {

    private Context mContext;

    public AbsMonitor(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Application app) {

    }

    @Override
    public void onDestroy() {

    }

    public String getAction() {
        return getActionFromAnnotation();
    }

    private String getActionFromAnnotation() {
        Action action = getClass().getAnnotation(Action.class);
        if (action != null) {
            return action.value();
        }
        return null;
    }


    @Override
    public int hashCode() {
        return AbsMonitor.class.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return AbsMonitor.class.getName().equals(obj.getClass().getName());
    }
}
