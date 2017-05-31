package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private MediaPlayer mediaPlayer;

    FrameLayout mainFrame, settingsFrame, languageFrame;
    FrameLayout currentFrame;

    private boolean musicPlaying = true;


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
        final ImageView backgroundOne = (ImageView) findViewById(R.id.main_imgvw_backgroundOne);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.main_imgvw_backgroundTwo);
        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        //setting animator up
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(100000L);
        //actual method
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        this.playBtn = (Button) findViewById(R.id.main_btn_play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(i);
            }
        });

        Button settingBtn = (Button) findViewById(R.id.main_btn_settings);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(mainFrame, settingsFrame, 0);
            }
        });

        Button muteBtn = (Button) findViewById(R.id.main_btn_mute);
        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlaying = !musicPlaying;

                if (musicPlaying) {
                    mediaPlayer.start();
                }
                if (!musicPlaying) {
                    mediaPlayer.pause();
                }
            }
        });

        Button languageBtn = (Button) findViewById(R.id.main_btn_language);
        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(settingsFrame, languageFrame, 0);
            }
        });

        Button dutchBtn= (Button) findViewById(R.id.main_btn_dutch);
        dutchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration = new Configuration(getResources().getConfiguration());
                configuration.locale = new Locale("nl");
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                recreate();
            }
        });

        Button englishBtn= (Button) findViewById(R.id.main_btn_english);
        englishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration = new Configuration(getResources().getConfiguration());
                configuration.locale = new Locale("def");
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                recreate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (currentFrame.equals(settingsFrame)) {
            animate(settingsFrame,mainFrame, 1);
        } else if (currentFrame.equals(mainFrame)) {
            System.exit(0);
        } else if (currentFrame.equals(languageFrame)){
            animate(languageFrame, settingsFrame, 1);
        }
        currentFrame.bringToFront();
    }

    void initFrames() {
        mainFrame = (FrameLayout) findViewById(R.id.main_fl_mainMenu);
        currentFrame = mainFrame;

        settingsFrame = (FrameLayout) findViewById(R.id.main_fl_settingsMenu);
        settingsFrame.setVisibility(View.INVISIBLE);

        languageFrame = (FrameLayout) findViewById(R.id.main_fl_language);
        languageFrame.setVisibility(View.INVISIBLE);

    }

    private void animate(FrameLayout from, FrameLayout to, int dir){
        Animation aOut;
        Animation aIn;

        if(dir == 0){
            aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
            aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        } else {
            aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_right);
            aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_left);
        }

        aOut.reset();
        aOut.setFillAfter(true);

        aIn.reset();
        aIn.setFillAfter(true);

        from.clearAnimation();
        from.startAnimation(aOut);

        to.clearAnimation();
        to.startAnimation(aIn);

        currentFrame = to;
        currentFrame.bringToFront();
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
