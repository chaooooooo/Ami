package chao.app.debugtools;

import org.junit.Test;

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

        Util.convert2Resource(2131296420);
    }

    private static class A {
        int a = 0;
    }
}