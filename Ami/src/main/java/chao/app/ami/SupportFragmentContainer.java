package chao.app.ami;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import chao.app.ami.base.AMIActivity;

/**
 * chao.qin
 * 2016/1/11
 * <p>
 * 这个Activity 只存在一个Fragment
 */
public class SupportFragmentContainer extends AMIActivity {

    public static final String KEY_FRAGMENT = "fragment";

    private Class<? extends Fragment> mTargetFragment;

    public static Intent buildContainerIntent(Context context,Class fragment) {
        Intent intent = new Intent(context,SupportFragmentContainer.class);
        intent.putExtra(KEY_FRAGMENT,fragment);
        return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);

        Intent intent = getIntent();
        if (intent != null) {
            mTargetFragment = (Class<? extends Fragment>) intent.getSerializableExtra(KEY_FRAGMENT);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        try {
            Fragment fragment = mTargetFragment.newInstance();
            fragment.setArguments(getIntent().getExtras());
            transaction.replace(R.id.fragment, fragment, mTargetFragment.getName());
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
