package chao.app.debugtools;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import chao.app.ami.Ami;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.ami.text.TextManager;
import java.util.Random;

/**
 * @author chao.qin
 * @since 2017/8/9
 */
@LayoutID(R.layout.test_list_fragment)
public class TestListFragment extends AMISupportFragment implements AdapterView.OnItemClickListener {

    @Override
    public void setupView(View layout) {
        ListView listView = findView(R.id.list);
        listView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_list_item,R.id.ami_text, TextManager.getSPoetry().getContent()));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Ami.log("" + view + ", " + position);
        SystemClock.sleep((long) (100 + (new Random().nextInt(1000))));
    }
}
