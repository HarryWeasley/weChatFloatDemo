package demo.com.lgx.wechatfloatdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yhao.floatwindow.ActivityStack;

/**
 * Created by Harry on 2018/8/9.
 * desc:
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().add(this);
    }
}
