package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity
{
    private long speed = 100000L;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ImageView backgroundGrassOne = (ImageView)findViewById(R.id.settings_imgvw_backgroundOne);
        final ImageView backgroundGrassTwo = (ImageView)findViewById(R.id.settings_imgvw_backgroundTwo);
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f ,0.0f);
        //setting animator up
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(speed);
        //animator.setCurrentPlayTime(mainActivity.getCurrentTime());
        //actual method
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundGrassOne.getWidth();
                final float translationY = width * progress;
                backgroundGrassOne.setTranslationX(translationY);
                backgroundGrassTwo.setTranslationX(translationY - width);
            }
        });
        animator.start();
    }


}
