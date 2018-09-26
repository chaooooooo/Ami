package chao.app.debugtools;

import android.os.Bundle;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMIActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@LayoutID(R.layout.ami_home_layout)
public class MainActivity extends AMIActivity {

    private int[] ia = new int[]{1,2,3,4,5};
    private String[] sa = new String[]{"aaaaa", "bbbbb", "cccccc", "dddddd"};
    private ArrayList<String> slist = new ArrayList<>();
    private ArrayList<A> alist = new ArrayList<>();

    private Map<String, Integer> map = new HashMap<>();

    {
        slist.add("xxxxx");
        slist.add("yyyyy");
        slist.add("zzzzz");

        alist.add(new A(10));
        alist.add(new A(11));
        alist.add(new A(12));
        alist.add(new A(13));

        map.put("移动", 10086);
        map.put("联通", 10010);
        map.put("电信", 10000);
    }

    class A {
        int a;

        public A(int a) {
            this.a = a;
        }

        @Override
        public String toString() {
            return "A{" +
                    "a=" + a +
                    '}';
        }
    }

    @Override
    public void setupView(Bundle savedInstanceState) {
        super.setupView(savedInstanceState);
//        UI.show(this, TestPermissionFragment.class);


//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);//display = getWindowManager().getDefaultDisplay();display.getMetrics(dm)（把屏幕尺寸信息赋值给DisplayMetrics dm）;
//        Screen screen = new Screen(dm);
//        Ami.log(screen);


    }

}
