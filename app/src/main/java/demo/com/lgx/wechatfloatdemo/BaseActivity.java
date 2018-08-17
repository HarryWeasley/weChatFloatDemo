package demo.com.lgx.wechatfloatdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.yhao.floatwindow.ActivityStack;

/**
 * Created by Harry on 2018/8/9.
 * desc:
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().add(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getMyApplication().isBackNoPermission()) {
            MyApplication.getMyApplication().setBackNoPermission(false);
            new AlertDialog.Builder(this).setTitle("提示,你没有开启浮窗权限")
                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消", null).show();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().remove(this);
    }


}
