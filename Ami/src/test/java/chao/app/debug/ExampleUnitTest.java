package chao.app.debug;

import org.junit.Test;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Object[][] objects = new Object[10][];
        objects[1] = new Object[20];
        for (int i=0; i<20;i++) {
            objects[1][i] = "i = " + i;
        }
        System.out.println(Arrays.toString(objects[1]));
    }
}