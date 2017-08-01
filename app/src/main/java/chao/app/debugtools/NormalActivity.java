package chao.app.debugtools;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author chao.qin
 * @since 2017/3/24
 */

public class NormalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        System.out.println(getClass().getName() + " --> " + findViewById(android.R.id.content));


    }
}
