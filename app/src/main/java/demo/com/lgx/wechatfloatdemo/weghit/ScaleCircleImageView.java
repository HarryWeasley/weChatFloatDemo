package demo.com.lgx.wechatfloatdemo.weghit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Harry on 2018/8/9.
 * desc:
 */

public class ScaleCircleImageView extends AppCompatImageView {
    private RectF mRectF;
    private ScaleCircleAnimation scaleCircleAnimation;
    private Paint mPaint;
    private ScaleCircleListener listener;
    Bitmap src;
    private Xfermode xfermode;

    public ScaleCircleImageView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public ScaleCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public ScaleCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setDither(true);
        }
        if (mRectF == null) {
            mRectF = new RectF();
        }
        if (xfermode == null) {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        }
        if (scaleCircleAnimation != null) {
            int left = scaleCircleAnimation.getLeftX();
            int top = scaleCircleAnimation.getTopY();
            int right = scaleCircleAnimation.getRightX();
            int bottom = scaleCircleAnimation.getBottomY();
            float radius = scaleCircleAnimation.getRadius();
            mRectF.set(left, top, right, bottom);
//            canvas.clipRect(mRectF);
            canvas.drawRoundRect(mRectF, radius, radius, mPaint);
            //设置Xfermode
            mPaint.setXfermode(xfermode);
            //源图
            canvas.drawBitmap(src, 0, 0, mPaint);
            //还原Xfermode
            mPaint.setXfermode(null);

        }
    }

    private int width;


    public void startAnimation(Bitmap bitmap, int width) {

        if (animationParam == null) {
            throw new IllegalArgumentException("animationParam has  been init!");
        }
        this.width = width;
        src = bitmap;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(new ScaleCircleAnimation(animationParam.fromLeftX, animationParam.fromRightX, animationParam.fromTopY, animationParam.fromBottomY, animationParam.fromRadius),
                new ScaleCircleAnimation(animationParam.toLeftX, animationParam.toRightX, animationParam.toTopY, animationParam.toBottomY, animationParam.toRadius));
        valueAnimator.setEvaluator(new TypeEvaluator<ScaleCircleAnimation>() {
            @Override
            public ScaleCircleAnimation evaluate(float fraction, ScaleCircleAnimation startValue, ScaleCircleAnimation endValue) {
                int leftX = (int) (startValue.getLeftX() + fraction * (endValue.getLeftX() - startValue.getLeftX()));
                int topY = (int) (startValue.getTopY() + fraction * (endValue.getTopY() - startValue.getTopY()));
                int rightX = (int) (startValue.getRightX() + fraction * (endValue.getRightX() - startValue.getRightX()));
                int bottomY = (int) (startValue.getBottomY() + fraction * (endValue.getBottomY() - startValue.getBottomY()));
                float radius = (startValue.getRadius() + fraction * (endValue.getRadius() - startValue.getRadius()));
                return new ScaleCircleAnimation(leftX, rightX, topY, bottomY, radius);
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleCircleAnimation = (ScaleCircleAnimation) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }
        });
    }

    private  AnimationParam animationParam;

    public  AnimationParam createAnmiationParam() {
        return animationParam = new AnimationParam();
    }


    public  class AnimationParam {
        int fromLeftX;
        int fromRightX;
        int toLeftX;
        int toRightX;
        int fromTopY;
        int fromBottomY;
        int toTopY;
        int toBottomY;
        int fromRadius;
        int toRadius;


        public AnimationParam setFromLeftX(int fromLeftX) {
            this.fromLeftX = fromLeftX;
            return this;
        }

        public AnimationParam setFromRightX(int fromRightX) {
            this.fromRightX = fromRightX;
            return this;
        }

        public AnimationParam setToLeftX(int toLeftX) {
            this.toLeftX = toLeftX;
            return this;
        }

        public AnimationParam setToRightX(int toRightX) {
            this.toRightX = toRightX;
            return this;
        }

        public AnimationParam setFromTopY(int fromTopY) {
            this.fromTopY = fromTopY;
            return this;
        }

        public AnimationParam setFromBottomY(int fromBottomY) {
            this.fromBottomY = fromBottomY;
            return this;
        }

        public AnimationParam setToTopY(int toTopY) {
            this.toTopY = toTopY;
            return this;
        }

        public AnimationParam setToBottomY(int toBottomY) {
            this.toBottomY = toBottomY;
            return this;
        }

        public AnimationParam setFromRadius(int fromRadius) {
            this.fromRadius = fromRadius;
            return this;
        }

        public AnimationParam setToRadius(int toRadius) {
            this.toRadius = toRadius;
            return this;
        }
    }

    public void setScaleCircleListener(ScaleCircleListener listener) {
        this.listener = listener;

    }

    public interface ScaleCircleListener {
        void onAnimationEnd();
    }
}
