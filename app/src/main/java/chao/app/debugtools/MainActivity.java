package chao.app.debugtools;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import chao.app.ami.launcher.drawer.DrawerXmlID;
import chao.app.ami.proxy.ProxyManager;

@DrawerXmlID(R.raw.drawer)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(getClass().getName() + " --> " + findViewById(android.R.id.content));
//        DebugTools.show(this, SecondActivity.class);
        findViewById(R.id.text).setOnClickListener(this);
        updateText();
//        mLauncher.setDrawerId(R.raw.drawer);
//        mLauncher.setContentView(this, R.layout.activity_main);

        try {
            ProxyManager.setProxy("192.168.10.10", 8080);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateText() {
        String[] proxyInfo = ProxyManager.getProxy();
        TextView textView = (TextView) findViewById(R.id.text);
        if (proxyInfo != null) {
            textView.setText(proxyInfo[0] + ": " + proxyInfo[1]);
        } else {
            textView.setText("no proxy");
            try {
                ProxyManager.setProxy("192.168.10.10", 8080);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        updateText();
    }
}
