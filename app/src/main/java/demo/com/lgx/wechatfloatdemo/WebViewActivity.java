package demo.com.lgx.wechatfloatdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionUtil;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.Util;

import demo.com.lgx.wechatfloatdemo.weghit.ScaleCircleImageView;
import demo.com.lgx.wechatfloatdemo.weghit.SpecificPositionWebView;


/**
 * Created by Harry on 2018/8/8.
 * desc:
 */

public class WebViewActivity extends BaseActivity {

    private ProgressBar progressBar;
    private String url = "https://blog.csdn.net/HarryWeasley/article/details/73692467";
    //    private String url = "https://www.baidu.com";
    private ScaleCircleImageView frameLayout;
    private SpecificPositionWebView webView;
    private FrameLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        frameLayout = findViewById(R.id.parent);
        progressBar = findViewById(R.id.progress);
        webView = findViewById(R.id.web_view);
        parent = findViewById(R.id.frame_layout);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                int scrollY = MyApplication.getMyApplication().getScrollY();
                if (scrollY != 0) {
                    webView.scrollTo(0, scrollY);
                }

            }
        });

        webView.setOnScrollChangedCallback(new SpecificPositionWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                MyApplication.getMyApplication().setScrollY(t);
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (PermissionUtil.hasPermission(this)) {
            IFloatWindow old = FloatWindow.get("old");
            if (old == null) {
                IFloatWindow cancel2 = FloatWindow.get("cancel2");
                if (cancel2 == null) {
                    FloatWindow
                            .with(getApplicationContext())
                            .setTag("cancel2")
                            .setView(R.layout.layout_window)
                            .setCancelParam2(320)
//                .setFilter(true, RNActivity.class)
//                        .setPermissionListener(permissionListener)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }
                IFloatWindow cancel = FloatWindow.get("cancel");
                if (cancel == null) {
                    FloatWindow
                            .with(getApplicationContext())
                            .setTag("cancel")
//                        .setPermissionListener(permissionListener)
                            .setView(R.layout.layout_window)
                            .setCancelParam2(300)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }


                ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
                FloatWindow
                        .with(getApplicationContext())
                        .setTag("old")
                        .setView(imageView)
//                .setWidth(Screen.width, 0.2f) //设置悬浮控件宽高
//                .setHeight(Screen.width, 0.2f)
                        .setMoveType(MoveType.slide, 0, 0)
                        .setWidth(75)
                        .setFilter(false, WebViewActivity.class)
                        .setHeight(75)
                        .setX(Screen.width, 0.8f)  //设置控件初始位置
                        .setY(parent.getHeight() / 3)
                        .setParentHeight(parent.getHeight())
                        .setMoveStyle(300, new AccelerateInterpolator())
                        .setDesktopShow(false)
                        .build();
                old = FloatWindow.get("old");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), WebViewActivity.class));
                    }
                });
                startAnimation(old);
            } else {
                startAnimation(old);
            }
        } else {
            //没有浮窗权限
            startEmptyAnimation();

        }


    }

    private void startAnimation(IFloatWindow old) {
        //创建当前视图的bitmap
        View view = parent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        frameLayout
                .createAnmiationParam()
                .setFromLeftX(0)
                .setToLeftX(old.getmB().xOffset)
                .setFromRightX(parent.getWidth())
                .setToRightX(old.getmB().xOffset + old.getmB().mWidth)
                .setFromTopY(0)
                .setToTopY(old.getmB().yOffset)
                .setFromBottomY(parent.getHeight())
                .setFromRadius(0)
                .setToRadius(old.getmB().mWidth / 2)
                .setToBottomY(old.getmB().yOffset + old.getmB().mWidth);
        frameLayout.startAnimation(bitmap, old.getmB().mWidth);
        webView.setVisibility(View.GONE);
        frameLayout.setScaleCircleListener(new ScaleCircleImageView.ScaleCircleListener() {
            @Override
            public void onAnimationEnd() {
                finish();
            }

        });
    }


    private void startEmptyAnimation() {
        //创建当前视图的bitmap
        View view = parent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        int mWidth = Util.dp2px(this, 75);
        int xOffset = Util.getScreenWidth(this)-mWidth;
        int yOffset = parent.getHeight() / 3;
        frameLayout
                .createAnmiationParam()
                .setFromLeftX(0)
                .setToLeftX(xOffset)
                .setFromRightX(parent.getWidth())
                .setToRightX(xOffset + mWidth)
                .setFromTopY(0)
                .setToTopY(yOffset)
                .setFromBottomY(parent.getHeight())
                .setFromRadius(0)
                .setToRadius(mWidth / 2)
                .setToBottomY(yOffset + mWidth);
        frameLayout.startAnimation(bitmap, mWidth);
        webView.setVisibility(View.GONE);
        frameLayout.setScaleCircleListener(new ScaleCircleImageView.ScaleCircleListener() {
            @Override
            public void onAnimationEnd() {
                MyApplication.getMyApplication().setBackNoPermission(true);
                finish();
            }

        });
    }
}
