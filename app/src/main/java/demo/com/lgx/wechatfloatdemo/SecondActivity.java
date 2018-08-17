package demo.com.lgx.wechatfloatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Harry on 2018/8/8.
 * desc:
 */

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,WebViewActivity.class));
//                imageView.setTestNum();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
