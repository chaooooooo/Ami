package chao.app.ami.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public class AMIActivity extends AppCompatActivity implements IAMIActivity {

    private AMIActivityHelper mHelper = new AMIActivityHelper(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper.onCreate(savedInstanceState);
        setupView(savedInstanceState);
    }

    public void setupView(Bundle savedInstanceState) {
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
    public void onDestroy() {
        super.onDestroy();
        mHelper.onDestroy();
    }


    public <T extends View> T findView(int resId) {
        return mHelper.findView(resId);
    }
}
