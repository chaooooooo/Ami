package chao.app.debugtools;

import android.view.View;

import java.io.IOException;

import chao.app.ami.base.AMIFragment;
import chao.app.ami.base.AmiSimpleFragment;
import chao.app.ami.inject.InjectionManager;

/**
 * Created by qinchao on 2017/10/1.
 */

public class TestClassInjectionFragment extends AmiSimpleFragment {

    @Override
    public void setupView(View layout) {
        super.setupView(layout);
    }

    @Override
    public void onClick(View v) {
        try {
            InjectionManager im = new InjectionManager(getActivity());
            im.inject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
