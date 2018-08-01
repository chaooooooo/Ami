package chao.app.debugtools;

import android.view.View;

import chao.app.ami.Ami;
import chao.app.ami.base.AmiSimpleFragment;

/**
 * Created by qinchao on 2017/9/5.
 */

public class TestAmiSimpleFragment extends AmiSimpleFragment {

    @Override
    public void onClick(View v) {
        Ami.log("simple fragment clicked!!");
    }
}
