package chao.app.sample;

import chao.app.ami.interfaces.IInjection;

/**
 * Created by qinchao on 2017/10/1.
 */

public class B implements IInjection {

    private String b = "B";

    public String getB() {
        return b;
    }

    @Override
    public void inject() {
        System.out.println("This is B!");
    }
}
