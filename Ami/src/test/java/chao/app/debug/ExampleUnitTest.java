package chao.app.debug;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import chao.app.ami.utils.ReflectUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    ArrayList<Integer> intList = new ArrayList<>();

    @Test
    public void addition_isCorrect() throws Exception {
        Object[][] objects = new Object[10][];
        objects[1] = new Object[20];
        for (int i=0; i<20;i++) {
            objects[1][i] = "i = " + i;
        }
        System.out.println(Arrays.toString(objects[1]));


        Field field = ReflectUtil.getField(ExampleUnitTest.class, "intList");
        assert field != null;
        System.out.println(field.getName());
        System.out.println(field.getType());
        System.out.println(field.getGenericType());

        System.out.println(null == null);

        String value = "[hello, luqin, 10086, 123]";

        System.out.println("   Hi, luqin   ".trim());
        System.out.println(value.substring(1, value.length() - 1));
    }
}