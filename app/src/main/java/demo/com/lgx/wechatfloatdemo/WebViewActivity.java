package demo.com.lgx.wechatfloatdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;


/**
 * Created by Harry on 2018/8/8.
 * desc:
 */

public class WebViewActivity extends AppCompatActivity{

    private ProgressBar progressBar;
    private String url="https://blog.csdn.net/HarryWeasley/article/details/51955467";
//    private String url="https://www.baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView=findViewById(R.id.web_view);
        progressBar=findViewById(R.id.progress);
        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show();


        IFloatWindow old = FloatWindow.get("old");
        if (old == null) {
            ImageView imageView=new ImageView(this);
            imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
            FloatWindow
                    .with(getApplicationContext())
                    .setTag("old")
                    .setView(imageView)
//                .setWidth(Screen.width, 0.2f) //设置悬浮控件宽高
//                .setHeight(Screen.width, 0.2f)
                    .setMoveType(MoveType.slide, 15, 15)
                    .setWidth(50)
                    .setFilter(false,WebViewActivity.class)
                    .setHeight(50)
                    .setX(Screen.width, 0.8f)  //设置控件初始位置
                    .setY(Screen.height, 0.2f)
                    .setMoveStyle(300, new AccelerateInterpolator())
                    .setDesktopShow(false)
                    .build();
        }

    }
}
