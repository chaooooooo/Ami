package chao.app.sample;

import chao.app.ami.interfaces.IInjection;

/**
 * Created by qinchao on 2017/10/1.
 */

public class A implements IInjection{
    private int a = 1;

    public int getA() {
        return a;
    }

    @Override
    public void inject() {
        System.out.println("This is A!");
    }
}
