package chao.app.debugtools;

import org.junit.Test;

import java.util.ArrayList;

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

        ArrayList<Integer> list = new ArrayList<>();

        System.out.print(list instanceof ArrayList);

    }

    private static class A {
        int a = 0;
    }
}