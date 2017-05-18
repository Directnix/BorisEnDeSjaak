package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Timer;

public class GameActivity extends AppCompatActivity
{
    private long speed = 10000L;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //creating variables
        final ImageView backgroundGrassOne = (ImageView)findViewById(R.id.game_imgvw_backgroundOne);
        final ImageView backgroundGrassTwo = (ImageView)findViewById(R.id.game_imgvw_backgroundTwo);
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f ,1.0f);
        //setting animator up
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(speed);
        //actual method
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                final float progress = (float) animation.getAnimatedValue();
                final float height = backgroundGrassOne.getHeight();
                final float translationY = height * progress;
                backgroundGrassOne.setTranslationY(translationY);
                backgroundGrassTwo.setTranslationY(translationY - height);
            }
        });
        animator.start();

    }

}
