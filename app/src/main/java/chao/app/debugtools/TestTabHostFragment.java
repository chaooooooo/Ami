package chao.app.debugtools;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import chao.app.debugtools.tabhost.HomeFragment;
import chao.app.debugtools.tabhost.MoreFragment;
import chao.app.debugtools.tabhost.TabListener;


/**
 * @author chao.qin
 * @since 2017/7/30
 */

public class TestTabHostFragment extends Fragment {
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class,EditTextInputFragment.class,MoreFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_selector,R.drawable.tab_message_selector,R.drawable.tab_more_selector };

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "消息", "更多"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab_host_test_fragment, container, false);
        initView(layout);

        return layout;
    }

    /**
     * 初始化组件
     */
    private void initView(View layout){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(getActivity());

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) layout.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.setOnTabChangedListener(new TabListener());

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_host_test_fragment_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    public void onTabChanged(String tabId) {

    }
}
