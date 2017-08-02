package chao.app.ami;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import chao.app.debug.R;

/**
 * chao.qin
 * 2016/1/11
 * <p>
 * 这个Activity 只存在一个Fragment
 */
public class FragmentContainer extends Activity {

    public static final String KEY_FRAGMENT = "fragment";

    private Class<? extends Fragment> mTargetFragment;

    public static Intent buildContainerIntent(Context context,Class fragment) {
        Intent intent = new Intent(context,FragmentContainer.class);
        intent.putExtra(KEY_FRAGMENT,fragment);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);

        Intent intent = getIntent();
        if (intent != null) {
            mTargetFragment = (Class<? extends Fragment>) intent.getSerializableExtra(KEY_FRAGMENT);
        }

        FragmentManager manager = getFragmentManager();
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
