package chao.app.debugtools;

import org.junit.Test;

import java.lang.reflect.Array;

import chao.app.ami.utils.Util;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final A a = new A();

    @Test
    public void addition_isCorrect() throws Exception {
        int a[] = {0,1,2,3};


        System.out.println("isArray:" + a.getClass().getComponentType());
    }

    private static class A {
        int a = 0;
    }
}