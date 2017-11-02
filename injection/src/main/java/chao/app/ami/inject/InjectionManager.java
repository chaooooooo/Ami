package chao.app.ami.inject;

import android.content.Context;

import java.io.IOException;

import chao.app.ami.interfaces.IInjection;

/**
 * Created by qinchao on 2017/10/1.
 */

public class InjectionManager {

    private Context mContext;

    public InjectionManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void inject() throws IOException {
        InjectionDexClassLoader classLoader = new InjectionDexClassLoader(mContext, "/storage/self/primary/sample.aar", getClass().getClassLoader());

        try {
            Class clazz = classLoader.findClass("chao.app.sample.A");
            Object obj = clazz.newInstance();
            IInjection injection = (IInjection) obj;
            injection.inject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
