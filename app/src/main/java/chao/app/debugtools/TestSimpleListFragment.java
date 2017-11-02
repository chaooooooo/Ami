package chao.app.debugtools;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import chao.app.ami.Ami;
import chao.app.ami.base.AmiSimpleListFragment;

/**
 * Created by qinchao on 2017/9/5.
 */

public class TestSimpleListFragment extends AmiSimpleListFragment {

    private static final String[] LIST_TITLES = {"1", "2", "3", "4","5"};

    private static final String[] LIST_TITLES2 = {"1", "2", "3", "4","5"};

    int a = 10;

    float b = 10.0f;

    String s = "hello";

    @Override
    public Object getObjects() {
        return LIST_TITLES;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Ami.log("simple item clicked " + LIST_TITLES[position]);

        Ami.deepLog(this);
    }
}
