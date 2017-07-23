package chao.app.debug;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FragmentContainer extends AppCompatActivity {

    public static final String KEY_FRAGMENT = "fragment";

    private Class<?> mTargetFragment;

    private Fragment mAppFragment;
    private android.support.v4.app.Fragment mSupportFragment;


    private boolean checkTargetFragment() {
        if (mTargetFragment == null) {
            return false;
        }
        return Fragment.class.isAssignableFrom(mTargetFragment) || android.support.v4.app.Fragment.class.isAssignableFrom(mTargetFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);

        Intent intent = getIntent();
        if (intent != null) {
            mTargetFragment = (Class<?>) intent.getSerializableExtra(KEY_FRAGMENT);
        }

        checkTargetFragment();

        try {
            replaceFragment(mTargetFragment);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(String fragment) {
        try {
            replaceFragment(Class.forName(fragment));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void replaceFragment(Class<?> fragmentClazz) throws InstantiationException, IllegalAccessException {
        if (fragmentClazz == null) {
            return;
        }
        removeAppFragment();
        removeSupportFragment();
        if (Fragment.class.isAssignableFrom(fragmentClazz)) {
            addAppFragment(fragmentClazz);
        } else if (android.support.v4.app.Fragment.class.isAssignableFrom(fragmentClazz)) {
            addSupportFragment(fragmentClazz);
        }
    }

    private void addAppFragment(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        mAppFragment = (Fragment) clazz.newInstance();
        transaction.replace(R.id.fragment, mAppFragment, clazz.getSimpleName());
        transaction.commitAllowingStateLoss();

    }

    private void addSupportFragment(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        mSupportFragment = (android.support.v4.app.Fragment) clazz.newInstance();
        transaction.replace(R.id.fragment, mSupportFragment, clazz.getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    private void removeAppFragment() {
        if (mAppFragment == null) {
            return;
        }
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(mAppFragment);
        transaction.commitAllowingStateLoss();
        mAppFragment = null;
    }

    private void removeSupportFragment() {
        if (mSupportFragment == null) {
            return;
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(mSupportFragment);
        transaction.commitAllowingStateLoss();
        mSupportFragment = null;
    }
}
