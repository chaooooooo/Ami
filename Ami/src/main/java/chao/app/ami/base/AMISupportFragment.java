package chao.app.ami.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import chao.app.ami.Ami;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public abstract class AMISupportFragment extends Fragment implements IAMIFragment {
    private AMIFragmentHelper mHelper = new AMIFragmentHelper(this);
    private ActionBar actionBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHelper.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper.onCreate(savedInstanceState);
    }

    @Override
    public void setupTitle() {
        android.support.v7.app.ActionBar supportActionBar = getAppCompatActivity().getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getClass().getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = mHelper.onCreateView(inflater, container, savedInstanceState);
        actionBar = getAppCompatActivity().getSupportActionBar();
        return view;
    }

    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public void setTitle(String title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        } else if (getActivity() != null){
            getActivity().setTitle(title);
        }
    }

    public void setDisplayHomeAsUpEnabled(boolean enabled) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHelper.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHelper.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHelper.onDetach();
    }

    public Context getAppContext() {
        return Ami.getApp();
    }

    @Override
    public <T extends View> T findView(int resId) {
        return mHelper.findView(resId);
    }


    @Override
    public int getLayoutID() {
        return View.NO_ID;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }
}
