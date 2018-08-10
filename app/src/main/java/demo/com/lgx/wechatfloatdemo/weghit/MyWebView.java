package demo.com.lgx.wechatfloatdemo.weghit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.tencent.smtt.sdk.WebView;

/**
 * Created by Harry on 2018/8/9.
 * desc:
 */

public class MyWebView extends WebView {
    private RectF mRectF;
    private Path mPath;
    private ScaleCircleAnimation scaleCircleAnimation;
    private Paint mPaint;
    private ScaleCircleListener listener;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        if(mPath==null){
            mPath=new Path();

        }
        if (scaleCircleAnimation != null) {
            int left = scaleCircleAnimation.getX();
            int top = getTop() + scaleCircleAnimation.getY();
            int right = getWidth();
            int bottom = getBottom() - scaleCircleAnimation.getY() + 150;
            float radius = scaleCircleAnimation.getRadius();
            Log.i("radius", "radius=" + radius + "left=" + left + "right=" + right + "top=" + top + "bottom=" + bottom);
            mRectF.set(left, top, right, bottom);
//            canvas.clipRect(mRectF);
//            canvas.drawRoundRect(mRectF, radius, radius, mPaint);
            mPath.setFillType(Path.FillType.INVERSE_WINDING);
            mPath.addRoundRect(mRectF,radius,radius,Path.Direction.CW);
            canvas.drawPath(mPath,createPorterDuffClearPaint());
        }

        super.onDraw(canvas);
    }

    private Paint poterPaint;

    private Paint createPorterDuffClearPaint()
    {
        if(poterPaint==null){
            Paint paint = new Paint();

            paint.setColor(Color.TRANSPARENT);

            paint.setStyle(Paint.Style.FILL);

            paint.setAntiAlias(true);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            return paint;
        }else{
            return poterPaint;
        }

    }


    public void startAnimation(int fromX, int toX, int fromY, int toY, int fromRadius, int toRadius) {
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
