package demo.com.lgx.wechatfloatdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.w3c.dom.Text;

import demo.com.lgx.wechatfloatdemo.weghit.CornersWebView;
import demo.com.lgx.wechatfloatdemo.weghit.MyFrameLayout;
import demo.com.lgx.wechatfloatdemo.weghit.MyWebView;


/**
 * Created by Harry on 2018/8/8.
 * desc:
 */

public class WebViewActivity extends BaseActivity {

    private ProgressBar progressBar;
    private String url = "https://blog.csdn.net/HarryWeasley/article/details/51955467";
    //    private String url="https://www.baidu.com";
    private MyFrameLayout frameLayout;
    private MyWebView webView;
    private CornersWebView webView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        frameLayout = findViewById(R.id.parent);
        progressBar = findViewById(R.id.progress);
        webView = findViewById(R.id.web_view);
        webView2=findViewById(R.id.web_view2);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.loadUrl(url);
        webView2.setWebViewClient(new WebViewClient() {
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

        Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
//        IFloatWindow old = FloatWindow.get("old");
//        if (old == null) {
//            ImageView imageView = new ImageView(this);
//            imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
//            FloatWindow
//                    .with(getApplicationContext())
//                    .setTag("old")
//                    .setView(imageView)
////                .setWidth(Screen.width, 0.2f) //设置悬浮控件宽高
////                .setHeight(Screen.width, 0.2f)
//                    .setMoveType(MoveType.slide, 15, 15)
//                    .setWidth(50)
////                    .setFilter(false,WebViewActivity.class)
//                    .setHeight(50)
//                    .setX(Screen.width, 0.8f)  //设置控件初始位置
//                    .setY(Screen.height, 0.5f)
//                    .setMoveStyle(300, new AccelerateInterpolator())
//                    .setDesktopShow(false)
//                    .build();
//        } else {
//
//        }

        webView.startAnimation(0, frameLayout.getWidth() - 150, 0, frameLayout.getHeight() / 2, 0, 75);
//        frameLayout.setScaleCircleListener(new MyFrameLayout.ScaleCircleListener() {
//            @Override
//            public void onAnimationEnd() {
////                finish();
//            }
//        });


    }
}
