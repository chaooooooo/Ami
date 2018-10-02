package chao.app.ami.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public interface IAMIActivity {
    void onCreate(@Nullable Bundle savedInstanceState);


    void onStart();

    void onResume();

    void onPause();

    void onStop();


    void onDestroy();


    <T extends View> T findView(int resId);
}
