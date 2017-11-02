package chao.app.ami.inject;


import android.content.Context;

import dalvik.system.DexClassLoader;

/**
 * Created by qinchao on 2017/10/1.
 */

public class InjectionDexClassLoader extends DexClassLoader {


    public InjectionDexClassLoader(Context context, String dexPath, ClassLoader parent) {
        super(dexPath, context.getDir("dex",Context.MODE_PRIVATE).getPath(), context.getDir("lib", Context.MODE_PRIVATE).getPath(), parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
