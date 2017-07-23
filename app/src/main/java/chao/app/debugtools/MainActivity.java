package chao.app.debugtools;

import android.os.Bundle;

import chao.app.debug.launcher.drawer.DrawerLauncher;
import chao.app.debug.launcher.drawer.DrawerXmlID;

@DrawerXmlID(R.raw.drawer)
public class MainActivity extends DrawerLauncher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
