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
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mPaint.setDither(true);
        }
        if (mRectF == null) {
            mRectF = new RectF();
        }
        if (xfermode == null) {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        }
        if (scaleCircleAnimation != null) {
            setLayerType(LAYER_TYPE_SOFTWARE, null); //关闭硬件加速

            int left = scaleCircleAnimation.getX();
            int top = getTop() + scaleCircleAnimation.getY();
            int right = getWidth();
            int bottom = getBottom() - scaleCircleAnimation.getY() + 150;
            float radius = scaleCircleAnimation.getRadius();
            mRectF.set(left, top, right, bottom);
            canvas.clipRect(mRectF);
            canvas.drawRoundRect(mRectF, radius, radius, mPaint);
            //设置Xfermode
            mPaint.setXfermode(xfermode);

            //源图
            canvas.drawBitmap(src, 0, 0, mPaint);

            //还原Xfermode
            mPaint.setXfermode(null);
        }
    }

    public void startAnimation(int fromX, int toX, int fromY, int toY, int fromRadius, int toRadius, Bitmap bitmap) {
        src = bitmap;
        //默认不执行onDraw方法

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(new ScaleCircleAnimation(fromX, fromY, fromRadius),
                new ScaleCircleAnimation(toX, toY, toRadius));
        valueAnimator.setEvaluator(new TypeEvaluator<ScaleCircleAnimation>() {
            @Override
            public ScaleCircleAnimation evaluate(float fraction, ScaleCircleAnimation startValue, ScaleCircleAnimation endValue) {
                int x = (int) (startValue.getX() + fraction * (endValue.getX() - startValue.getX()));
                int y = (int) (startValue.getY() + fraction * (endValue.getY() - startValue.getY()));
                float radius = (startValue.getRadius() + fraction * (endValue.getRadius() - startValue.getRadius()));
                return new ScaleCircleAnimation(x, y, radius);
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

    public void setScaleCircleListener(ScaleCircleListener listener) {
        this.listener = listener;

    }

    public interface ScaleCircleListener {
        void onAnimationEnd();
    }
}
