package demo.com.lgx.wechatfloatdemo.weghit;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Harry on 2018/8/17.
 * desc:
 */

public class SpecificPositionWebView extends WebView{
    public SpecificPositionWebView(Context context) {
        super(context);
    }

    public SpecificPositionWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecificPositionWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private OnScrollChangedCallback mOnScrollChangedCallback;

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
    }

    public interface OnScrollChangedCallback {
        void onScroll(int l, int t, int oldl, int oldt);
    }

}
