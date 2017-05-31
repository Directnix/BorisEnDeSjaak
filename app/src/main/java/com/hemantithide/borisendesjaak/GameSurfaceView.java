package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Daniel on 23/05/2017.
 */

public class GameSurfaceView extends SurfaceView {

    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;

    private long speed = 7000L;
    private ImageView transparentView;

    GameActivity gameActivity;

    public GameSurfaceView(Context context) {
        super(context);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.surfaceHolder = getHolder();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = surfaceHolder.lockCanvas();

                //creating variables

                ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                //setting animator up

                final ImageView backgroundGrass1 = new ImageView(GameSurfaceView.super.getContext());
                final ImageView backgroundGrass2 = new ImageView(GameSurfaceView.super.getContext());

                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(speed);
                //actual method
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final float progress = (float) animation.getAnimatedValue();
                        final float height = backgroundGrass1.getHeight();
                        final float translationY = height * progress;
                        backgroundGrass1.setTranslationY(translationY);
                        backgroundGrass2.setTranslationY(translationY - height);
                    }
                });
                animator.start();

                updateCanvas(canvas);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    protected void updateCanvas(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawColor(Color.LTGRAY);
        canvas.drawCircle(100, 100, 50, paint);
    }

}