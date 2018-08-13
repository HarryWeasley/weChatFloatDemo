package demo.com.lgx.wechatfloatdemo.weghit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.function.ToDoubleFunction;

/**
 * Created by Harry on 2018/8/9.
 * desc:
 */

public class MyFrameLayout extends FrameLayout {
    private RectF mRectF;
    private ScaleCircleAnimation scaleCircleAnimation=new ScaleCircleAnimation(0,0,5);
    private Paint mPaint;
    private ScaleCircleListener listener;

    public MyFrameLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setDither(true);
        }
        if (mRectF == null) {
            mRectF = new RectF();
        }
        if (scaleCircleAnimation != null) {
            int left = scaleCircleAnimation.getX();
            int top = getTop() + scaleCircleAnimation.getY();
            int right = getWidth();
            int bottom = getBottom() - scaleCircleAnimation.getY() + 150;
            float radius = scaleCircleAnimation.getRadius();
            Log.i("radius", "radius=" + radius + "left=" + left + "right=" + right + "top=" + top + "bottom=" + bottom);
            mRectF.set(left, top, right, bottom);
            canvas.clipRect(mRectF);

//            setPadding(left, top, right, bottom);
            canvas.drawRoundRect(mRectF, radius, radius, mPaint);
        }
        super.onDraw(canvas);
    }


    public void startAnimation(int fromX, int toX, int fromY, int toY, int fromRadius, int toRadius) {
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
        valueAnimator.setDuration(5000);
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
