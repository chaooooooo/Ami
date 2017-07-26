package chao.app.debugtools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import chao.app.debug.launcher.drawer.DrawerXmlID;

@DrawerXmlID(R.raw.drawer)
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(getClass().getName() + " --> " + findViewById(android.R.id.content));
//        DebugTools.show(this, SecondActivity.class);

//        mLauncher.setDrawerId(R.raw.drawer);
//        mLauncher.setContentView(this, R.layout.activity_main);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
