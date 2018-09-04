package chao.app.debugtools;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.ami.utils.permission.PermissionHelper;
import chao.app.ami.utils.permission.PermissionListener;

/**
 * @author qinchao
 * @since 2018/8/30
 */
@LayoutID(R.layout.empty_test_fragment)
public class TestPermissionFragment extends AMISupportFragment {
    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断

    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode


    @Override
    public void setupView(View layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTelClick(v);
            }
        });
    }


    public void getTelClick(View v) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
//                    0);//申请权限
//            } else {//拥有当前权限
//            }
//        } else {
//            Toast.makeText(getActivity(), "6.0以下", Toast.LENGTH_SHORT).show();
//        }

        PermissionHelper.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_CONTACTS}, new PermissionListener() {
            @Override
            public void onPassed() {
                Toast.makeText(getContext(), "权限请求成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDenied(boolean neverAsk) {
                Toast.makeText(getContext(), "权限请求失败", Toast.LENGTH_SHORT).show();
                return super.onDenied(neverAsk);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "允许有权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}
