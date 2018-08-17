package demo.com.lgx.wechatfloatdemo;

import android.app.Application;

/**
 * Created by Harry on 2018/8/8.
 * desc:
 */

public class MyApplication extends Application {


    static MyApplication myApplication;

    private int scrollY;
    //是否是从webViewActivity界面返回，并且没有浮窗权限
    private boolean isBackNoPermission;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public boolean isBackNoPermission() {
        return isBackNoPermission;
    }

    public void setBackNoPermission(boolean backNoPermission) {
        isBackNoPermission = backNoPermission;
    }
}
