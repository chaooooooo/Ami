package chao.app.ami.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public interface IAMIFragment {
    void onAttach(Context context);

    void onCreate(@Nullable Bundle savedInstanceState);

    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void onStart();

    void onActivityCreated(@Nullable Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onStop();

    void onDestroyView();

    void onDestroy();

    void onDetach();

    <T extends View> T findView(int resId);
}
