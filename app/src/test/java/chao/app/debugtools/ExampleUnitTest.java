package chao.app.debugtools;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


        String patten = "   100      |10086   ";
        String[] ps = patten.split("\\s*|\\s*");
        System.out.println(patten.trim());
    }
}