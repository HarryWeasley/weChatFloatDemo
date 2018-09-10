package com.yhao.floatwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by yhao on 2017/12/22.
 * https://github.com/yhaolpz
 */

public class IFloatWindowImpl extends IFloatWindow {


    private FloatWindow.B mB;
    private FloatView mFloatView;
    private FloatLifecycle mFloatLifecycle;
    private boolean isShow;
    private boolean once = true;
    private ValueAnimator mAnimator;
    private TimeInterpolator mDecelerateInterpolator;
    private float downX;
    private float downY;
    private float upX;
    private float upY;
    private boolean mClick = false;
    private int mSlop;
    //消失view的x和y坐标
    private int xCancelOffset, yCancelOffset;
    //屏幕右下角的x\y坐标
    private int xCoordinate, yCoordinate;
    //屏幕高度，总视图的高度
    private int screenWidth, parentHeight;

    IFloatWindowImpl(FloatWindow.B b) {
        screenWidth = Util.getScreenWidth(b.mApplicationContext) - b.mWidth;
        parentHeight = b.parentHeight - b.mHeight;
        mB = b;
        if (mB.mMoveType == MoveType.fixed) {
            //7.0 以下采用自定义 toast, 7.1 及以上引导用户申请权限
            //这里的代码不执行，因为没有用MoveType.fixed
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                mFloatView = new FloatPhone(b.mApplicationContext, mB.mPermissionListener);
            } else {
                mFloatView = new FloatToast(b.mApplicationContext);
            }
        } else {
            //都是执行的这里的代码，记住
            mFloatView = new FloatPhone(b.mApplicationContext, mB.mPermissionListener);
            initTouchEvent();
        }
        mFloatView.setSize(mB.mWidth, mB.mHeight);
        mFloatView.setGravity(mB.gravity, mB.xOffset, mB.yOffset);
        mFloatView.setView(mB.mView);
        if (mB.mTag.equals("old")) {
            mFloatLifecycle = new FloatLifecycle(mB.mApplicationContext, mB.mShow, mB.mActivities, new LifecycleListener() {
                @Override
                public void onShow() {
                    show();
                }

                @Override
                public void onHide() {
                    hide();
                }

                @Override
                public void onBackToDesktop() {
                    if (!mB.mDesktopShow) {
                        hide();
                    }
                    if (mB.mViewStateListener != null) {
                        mB.mViewStateListener.onBackToDesktop();
                    }
                }
            });
        } else {
            mFloatView.init();
            once = false;
            getView().setVisibility(View.INVISIBLE);
        }


    }

    public FloatWindow.B getmB() {
        return mB;
    }


    public boolean isShow() {
        return isShow;
    }

    @Override
    int[] getOffset() {
        int[] array = new int[2];
        array[0] = mB.xOffset;
        array[1] = mB.yOffset;
        return array;
    }


    @Override
    public void show() {
        LogUtil.e(mB.mTag);
        if (once) {
            mFloatView.init();
            once = false;
            isShow = true;
        } else {
            if (isShow) {
                return;
            }
            getView().setVisibility(View.VISIBLE);
            isShow = true;
        }
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onShow();
        }
//        IFloatWindow oldWindow=FloatWindow.get("old");
//        if(oldWindow!=null){
//            oldWindow.hide();
//        }
//        IFloatWindow cancelWindow = FloatWindow.get("cancel");
//        if (cancelWindow != null) {
//            cancelWindow.hide();
//        }
//        IFloatWindow cancelWindow2 = FloatWindow.get("cancel2");
//        if (cancelWindow2 != null) {
//            cancelWindow2.hide();
//        }

    }

    @Override
    public void showCancel(boolean isBig) {
        if (once) {
            mFloatView.init();
            once = false;
            isShow = true;
        } else {
            if (isShow) {
                return;
            }
            getView().setVisibility(View.VISIBLE);
            isShow = true;
            if (!isBig) {
                showWithAnimator(true);
            }

        }
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onShow();
        }

    }

    @Override
    public void showCancelBig() {
        if (once) {
            mFloatView.init();
            once = false;
            isShow = true;
        } else {
            if (isShow) {
                return;
            }
            IFloatWindow cancelWindow = FloatWindow.get("cancel");
            if (cancelWindow != null) {
                if (cancelWindow.isShow()) {
                    //如果小的显示出来后，才显示大的出来
                    getView().setVisibility(View.VISIBLE);
                    isShow = true;
                }
            }
        }
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onShow();
        }
    }


    @Override
    public void hideCancel(boolean isBig) {
        if (once || !isShow) {
            return;
        }
        if (!isBig) {
            showWithAnimator(false);
        } else {
            getView().setVisibility(View.INVISIBLE);
        }
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onHide();
        }

    }

    private void showWithAnimator(final boolean isShow) {
        if (xCancelOffset == 0) {
            IFloatWindow cancelWindow = FloatWindow.get("cancel");
            if (cancelWindow != null) {
                int[] array = cancelWindow.getOffset();
                xCancelOffset = array[0];
                yCancelOffset = array[1];
            }
        }
        if (xCoordinate == 0) {
            xCoordinate = Util.getScreenWidth(mB.mApplicationContext);
            yCoordinate = Util.getScreenHeight(mB.mApplicationContext);
        }
        ValueAnimator mAnimator = new ValueAnimator();
        mAnimator.setDuration(500);
        if (isShow) {
            mAnimator.setObjectValues(new PointF(xCoordinate, yCoordinate), new PointF(xCancelOffset, yCancelOffset));
        } else {
            mAnimator.setObjectValues(new PointF(xCancelOffset, yCancelOffset), new PointF(xCoordinate, yCoordinate));
        }

        mAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                int valueX = (int) (startValue.getX() + fraction * (endValue.getX() - startValue.getX()));
                int valueY = (int) (startValue.getY() + fraction * (endValue.getY() - startValue.getY()));
                return new PointF(valueX, valueY);
            }
        });
        mAnimator.start();
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();

                mFloatView.updateXY(point.getX(), point.getY());

            }
        });
    }

    class PointF {
        int x;
        int y;

        PointF(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    @Override
    public void hide() {
        if (once || !isShow) {
            return;
        }
        getView().setVisibility(View.INVISIBLE);
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onHide();
        }
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public void dismiss() {
        mFloatView.dismiss();
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onDismiss();
        }
    }

    @Override
    public void updateX(int x) {
        checkMoveType();
        mB.xOffset = x;
        mFloatView.updateX(x);
    }

    @Override
    public void updateY(int y) {
        checkMoveType();
        mB.yOffset = y;
        mFloatView.updateY(y);
    }

    @Override
    public void updateX(int screenType, float ratio) {
        checkMoveType();
        mB.xOffset = (int) ((screenType == Screen.width ?
                Util.getScreenWidth(mB.mApplicationContext) :
                Util.getScreenHeight(mB.mApplicationContext)) * ratio);
        mFloatView.updateX(mB.xOffset);

    }

    @Override
    public void updateY(int screenType, float ratio) {
        checkMoveType();
        mB.yOffset = (int) ((screenType == Screen.width ?
                Util.getScreenWidth(mB.mApplicationContext) :
                Util.getScreenHeight(mB.mApplicationContext)) * ratio);
        mFloatView.updateY(mB.yOffset);

    }

    @Override
    public int getX() {
        return mFloatView.getX();
    }

    @Override
    public int getY() {
        return mFloatView.getY();
    }


    @Override
    public View getView() {
        mSlop = ViewConfiguration.get(mB.mApplicationContext).getScaledTouchSlop();
        return mB.mView;
    }


    private void checkMoveType() {
        if (mB.mMoveType == MoveType.fixed) {
            throw new IllegalArgumentException("FloatWindow of this tag is not allowed to move!");
        }
    }


    private void initTouchEvent() {
        switch (mB.mMoveType) {
            case MoveType.inactive:
                break;
            default:
                getView().setOnTouchListener(new View.OnTouchListener() {
                    float lastX, lastY, changeX, changeY;
                    int newX, newY;

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                downX = event.getRawX();
                                downY = event.getRawY();
                                lastX = event.getRawX();
                                lastY = event.getRawY();
                                cancelAnimator();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                changeX = event.getRawX() - lastX;
                                changeY = event.getRawY() - lastY;
                                newX = (int) (mFloatView.getX() + changeX);
                                newY = (int) (mFloatView.getY() + changeY);

                                if (newX <= 0) {
                                    newX = 0;
                                }
                                if (newX >= screenWidth) {
                                    newX = screenWidth;
                                }
                                if (newY <= 0) {
                                    newY = 0;
                                }
                                if (newY >= parentHeight) {
                                    newY = parentHeight;
                                }
                                mFloatView.updateXY(newX, newY);
                                if (mB.mViewStateListener != null) {
//                                    mB.mViewStateListener.onPositionUpdate(newX, newY);
                                }
                                lastX = event.getRawX();
                                lastY = event.getRawY();

                                if (changeX > 5 || changeY > 5 || changeX < -5 || changeY < -5) {
                                    if (mB.mMoveType == MoveType.slide) {
                                        if (mB.mViewStateListener != null) {
                                            mB.mViewStateListener.onMoveAnimStart();
                                        }
                                        IFloatWindow cancelWindow = FloatWindow.get("cancel");
                                        if (cancelWindow != null) {
                                            cancelWindow.showCancel(false);
                                        }
                                    }
                                    biggenView();
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                upX = event.getRawX();
                                upY = event.getRawY();
                                mClick = (Math.abs(upX - downX) > mSlop) || (Math.abs(upY - downY) > mSlop);
                                switch (mB.mMoveType) {
                                    case MoveType.slide:
                                        int startX = mFloatView.getX();
                                        int endX = (startX * 2 + v.getWidth() > Util.getScreenWidth(mB.mApplicationContext)) ?
                                                Util.getScreenWidth(mB.mApplicationContext) - v.getWidth() - mB.mSlideRightMargin :
                                                mB.mSlideLeftMargin;
                                        mB.xOffset = endX;
                                        mB.yOffset = newY;
                                        mAnimator = ObjectAnimator.ofInt(startX, endX);
                                        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                int x = (int) animation.getAnimatedValue();
                                                mFloatView.updateX(x);
                                                if (mB.mViewStateListener != null) {
                                                    mB.mViewStateListener.onPositionUpdate(x, (int) upY);
                                                }
                                            }
                                        });
                                        startAnimator();
                                        if (xCancelOffset == 0) {
                                            IFloatWindow cancelWindow = FloatWindow.get("cancel");
                                            if (cancelWindow != null) {
                                                int[] array = cancelWindow.getOffset();
                                                xCancelOffset = array[0];
                                                yCancelOffset = array[1];
                                            }
                                        }
                                        if (upX > xCancelOffset && upY > yCancelOffset) {
                                            IFloatWindow cancelWindow2 = FloatWindow.get("cancel2");
                                            if (cancelWindow2 != null) {
                                                if (cancelWindow2.getView().getVisibility() == View.VISIBLE) {
                                                    //只有在大的消失框出现的时候，才会消失浮窗
                                                    cancelWindow2.hideCancel(true);
                                                    if (mB.mViewStateListener != null) {
                                                        mB.mViewStateListener.onCancelHide();
                                                    }
                                                    IFloatWindow cancelWindow5 = FloatWindow.get("cancel");
                                                    if (cancelWindow5 != null) {
                                                        cancelWindow5.hideCancel(true);
                                                    }


                                                    FloatWindow.destroy("old");
                                                    mB.mView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                                                }

                                            }


                                        }
                                        break;
                                    case MoveType.back:
                                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("x", mFloatView.getX(), mB.xOffset);
                                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("y", mFloatView.getY(), mB.yOffset);
                                        mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY);
                                        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                int x = (int) animation.getAnimatedValue("x");
                                                int y = (int) animation.getAnimatedValue("y");
                                                mFloatView.updateXY(x, y);
                                                if (mB.mViewStateListener != null) {
                                                    mB.mViewStateListener.onPositionUpdate(x, y);
                                                }
                                            }
                                        });
                                        startAnimator();
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                        return mClick;
                    }

                    private void biggenView() {
                        if (xCancelOffset == 0) {
                            IFloatWindow cancelWindow = FloatWindow.get("cancel");
                            if (cancelWindow != null) {
                                int[] array = cancelWindow.getOffset();
                                xCancelOffset = array[0];
                                yCancelOffset = array[1];
                            }
                        }
                        if (newX > xCancelOffset && newY > yCancelOffset) {
                            IFloatWindow cancelWindow = FloatWindow.get("cancel2");
                            if (cancelWindow != null) {
                                cancelWindow.showCancelBig();
                            }
                        } else {

                            IFloatWindow cancelWindow = FloatWindow.get("cancel2");
                            if (cancelWindow != null) {
                                cancelWindow.hideCancel(true);
                            }
                        }
                    }
                });
        }
    }


    private void startAnimator() {
        if (mB.mInterpolator == null) {
            if (mDecelerateInterpolator == null) {
                mDecelerateInterpolator = new DecelerateInterpolator();
            }
            mB.mInterpolator = mDecelerateInterpolator;
        }
        mAnimator.setInterpolator(mB.mInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
                if (mB.mViewStateListener != null) {
                    mB.mViewStateListener.onMoveAnimEnd();
                }
                IFloatWindow cancelWindow5 = FloatWindow.get("cancel");
                if (cancelWindow5 != null) {
                    cancelWindow5.hideCancel(false);
                }

                IFloatWindow cancelWindow2 = FloatWindow.get("cancel2");
                if (cancelWindow2 != null) {
                    cancelWindow2.hideCancel(true);
                }
            }
        });
        mAnimator.setDuration(mB.mDuration).start();
//        if (mB.mViewStateListener != null) {
//            mB.mViewStateListener.onMoveAnimStart();
//        }
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
