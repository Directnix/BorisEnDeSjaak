package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private MediaPlayer mediaPlayer;

    FrameLayout playFrame, settingsFrame;
    FrameLayout currentFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start music loop
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sjaaksong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        initFrames();

        //creating variables
        final ImageView backgroundOne = (ImageView)findViewById(R.id.main_imgvw_backgroundOne);
        final ImageView backgroundTwo = (ImageView)findViewById(R.id.main_imgvw_backgroundTwo);
        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f ,0.0f);
        //setting animator up
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(100000L);
        //actual method
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        this.playBtn = (Button) findViewById(R.id.main_btn_play);
        playBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Animation aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
                aOut.reset();
                aOut.setFillAfter(true);

                Animation aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
                aIn.reset();
                aIn.setFillAfter(true);

                playFrame.clearAnimation();
                playFrame.startAnimation(aOut);

                settingsFrame.clearAnimation();
                settingsFrame.startAnimation(aIn);

                currentFrame = settingsFrame;
                //Intent i = new Intent(getApplicationContext(), GameActivity.class);
                //startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(currentFrame.equals(settingsFrame)){
            Animation aOut = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.out_right);
            aOut.reset();
            aOut.setFillAfter(true);

            Animation aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_left);
            aIn.reset();
            aIn.setFillAfter(true);

            settingsFrame.clearAnimation();
            settingsFrame.startAnimation(aOut);

            playFrame.clearAnimation();
            playFrame.startAnimation(aIn);

            currentFrame = playFrame;
        }
    }

    void initFrames(){
        playFrame = (FrameLayout) findViewById(R.id.main_fl_playMenu);
        currentFrame = playFrame;

        settingsFrame = (FrameLayout) findViewById(R.id.main_fl_settingsMenu);
        settingsFrame.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }
}
