package chao.app.debugtools;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final A a = new A();

    @Test
    public void addition_isCorrect() throws Exception {

        System.out.println(a);

        Field field = ExampleUnitTest.class.getDeclaredField("a");
        field.setAccessible(true);
        field.set(this, new A());
        System.out.println(a);
    }

    private static class A {
        int a = 0;
    }
}