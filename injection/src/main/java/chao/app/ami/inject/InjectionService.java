package chao.app.ami.inject;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by qinchao on 2017/10/1.
 */

public class InjectionService extends IntentService {

    public InjectionService() {
        super("InjectionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        String name = intent.getStringExtra("name");
        String alias = intent.getStringExtra("alias");

//        InjectionManager im = new InjectionManager();
    }
}
