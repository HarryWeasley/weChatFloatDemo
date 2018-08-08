package com.yhao.floatwindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于在内部自动申请权限
 * https://github.com/yhaolpz
 */

public class FloatActivity extends Activity {

    private static List<PermissionListener> mPermissionListenerList;
    private static PermissionListener mPermissionListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAlertWindowPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 756232212);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 756232212) {
            if (PermissionUtil.hasPermissionOnActivityResult(this)) {
                if(mPermissionListener!=null){
                    mPermissionListener.onSuccess();
                }
            } else {
                if(mPermissionListener!=null){
                    mPermissionListener.onFail();
                }
            }
        }
        finish();
    }

    public static synchronized void request(final Context context, final PermissionListener permissionListener) {
        if (PermissionUtil.hasPermission(context)) {
            permissionListener.onSuccess();
        } else {
            if (mPermissionListenerList == null) {
                mPermissionListenerList = new ArrayList<>();
                mPermissionListener = new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        for (PermissionListener listener : mPermissionListenerList) {
                            listener.onSuccess();
                        }
                        mPermissionListenerList.clear();
                    }

                    @Override
                    public void onFail() {
                        for (PermissionListener listener : mPermissionListenerList) {
                            listener.onFail();
                        }
                        mPermissionListenerList.clear();
                    }
                };
                Intent intent = new Intent(context, FloatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            mPermissionListenerList.add(permissionListener);


//            new AlertDialog.Builder(context)
//                    .setCancelable(false)
//                    .setMessage("应用需要获取悬浮窗权限，是否要开启")
//                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            if (mPermissionListenerList == null) {
//                                mPermissionListenerList = new ArrayList<>();
//                                mPermissionListener = new PermissionListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                        for (PermissionListener listener : mPermissionListenerList) {
//                                            listener.onSuccess();
//                                        }
//                                        mPermissionListenerList.clear();
//                                    }
//
//                                    @Override
//                                    public void onFail() {
//                                        for (PermissionListener listener : mPermissionListenerList) {
//                                            listener.onFail();
//                                        }
//                                        mPermissionListenerList.clear();
//                                    }
//                                };
//                                Intent intent = new Intent(context, FloatActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);
//                            }
//                            mPermissionListenerList.add(permissionListener);
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    for (PermissionListener listener : mPermissionListenerList) {
//                        listener.onFail();
//                    }
//                    mPermissionListenerList.clear();
//                }
//            });
        }

    }


    public static synchronized void request2(final Context context, final PermissionListener permissionListener) {
        if (PermissionUtil.hasPermission(context)) {
            permissionListener.onSuccess();
        } else {
         mPermissionListener= new PermissionListener() {
                    @Override
                    public void onSuccess() {
//                        for (PermissionListener listener : mPermissionListenerList) {
//                            listener.onSuccess();
//                        }
//                        mPermissionListenerList.clear();
                        permissionListener.onSuccess();
                    }

                    @Override
                    public void onFail() {
//                        for (PermissionListener listener : mPermissionListenerList) {
//                            listener.onFail();
//                        }
//                        mPermissionListenerList.clear();
                        permissionListener.onFail();
                    }
                };
                Intent intent = new Intent(context, FloatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


//            new AlertDialog.Builder(context)
//                    .setCancelable(false)
//                    .setMessage("应用需要获取悬浮窗权限，是否要开启")
//                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            if (mPermissionListenerList == null) {
//                                mPermissionListenerList = new ArrayList<>();
//                                mPermissionListener = new PermissionListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                        for (PermissionListener listener : mPermissionListenerList) {
//                                            listener.onSuccess();
//                                        }
//                                        mPermissionListenerList.clear();
//                                    }
//
//                                    @Override
//                                    public void onFail() {
//                                        for (PermissionListener listener : mPermissionListenerList) {
//                                            listener.onFail();
//                                        }
//                                        mPermissionListenerList.clear();
//                                    }
//                                };
//                                Intent intent = new Intent(context, FloatActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);
//                            }
//                            mPermissionListenerList.add(permissionListener);
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    for (PermissionListener listener : mPermissionListenerList) {
//                        listener.onFail();
//                    }
//                    mPermissionListenerList.clear();
//                }
//            });
        }

    }


}
