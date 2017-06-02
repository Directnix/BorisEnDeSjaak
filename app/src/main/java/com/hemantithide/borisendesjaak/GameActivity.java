package com.hemantithide.borisendesjaak;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;






/*      TODO
        - pause screen
        - pause on leaving the app
        - mockup settings menu
        - uitwerking settings menu
        - random crash door soundPlayer fixen
        - hide notifications during game session
        - moeilijkheid baseren op door de speler ingevoerde leeftijd
        - sla behaalde punten lokaal op, haal username rerolls van die punten (achievements mogelijk!)
        - bij eerste opstart: de speler keuze geven uit 3 à 5 randomly generated usernames
*/





public class GameActivity extends AppCompatActivity
{
    public String username;

    public enum Sound {
        ROCK_HIT,
        SWIPE,
        WOOSH,
        AYO_WHADDUP,
        POWERUP,
        POWERUP_LOOP,
        KINKER,
        BORIS_CHARGE,
        FIREBALL,
        FIRE_ON_ROCK,
        SHEEP_SCREECH
    }

    private GameSurfaceView surfaceView;
    private ImageView transparentView;

    public TextView frameCounter;

    MediaPlayer mediaPlayer;
    MediaPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        username = (String) getIntent().getSerializableExtra("USERNAME");

        // music
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ingamesong);
        mediaPlayer.setLooping(true);

        if (MainActivity.musicPlaying)
            mediaPlayer.start();
        else
            mediaPlayer.pause();

        // sound effects
        soundPlayer = MediaPlayer.create(getApplicationContext(), R.raw.swipe);
        soundPlayer.setLooping(false);

//        //creating background imageviews
//        final ImageView backgroundGrassOne = (ImageView)findViewById(R.id.game_imgvw_backgroundOne);
//        final ImageView backgroundGrassTwo = (ImageView)findViewById(R.id.game_imgvw_backgroundTwo);

        // game surface view init
        surfaceView = (GameSurfaceView)findViewById(R.id.game_srfcvw);
//        surfaceView.setBackgroundImageView(backgroundGrassOne, backgroundGrassTwo);

        frameCounter = (TextView)findViewById(R.id.game_txtvw_counter);
        surfaceView.setFrameCounter(frameCounter);

        surfaceView.setActivity(this);

        // give metrics to surface view
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        surfaceView.setMetrics(metrics);

        // Swipe
        transparentView = (ImageView)findViewById(R.id.game_imgvw_transparent);
        transparentView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

            @Override
            public void onSwipeLeft() {
                surfaceView.onSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                surfaceView.onSwipeRight();
            }

            @Override
            public void onSwipeDown() {
                surfaceView.onSwipeDown();
            }

            @Override
            public void onSwipeUp() {
                surfaceView.onSwipeUp();
            }
        });

        // disable notifications
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
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

    public void playSound(Sound sound) {

        int input = R.raw.swipe;

        switch(sound) {
            case SWIPE:
                input = R.raw.swipe;
                break;
            case ROCK_HIT:
                input = R.raw.rock_hit;
                break;
            case WOOSH:
                input = R.raw.woosh;
                break;
            case AYO_WHADDUP:
                input = R.raw.ayo_whaddup;
                break;
            case POWERUP:
                input = R.raw.powerup;
                break;
            case POWERUP_LOOP:
                input = R.raw.powerup_active;
                break;
            case KINKER:
                input = R.raw.kinker;
                break;
            case BORIS_CHARGE:
                input = R.raw.boris_charge;
                break;
            case FIREBALL:
                input = R.raw.fireball;
                break;
            case FIRE_ON_ROCK:
                input = R.raw.fire_on_rock;
                break;
            case SHEEP_SCREECH:
                input = R.raw.sheep_screech;
                break;
        }

        soundPlayer = MediaPlayer.create(getApplicationContext(), input);
        soundPlayer.start();

        try {
            if (soundPlayer != null && soundPlayer.isPlaying()) {
                soundPlayer.seekTo(0);

                soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                    }
                });
            }
        } catch (NullPointerException ignored){}

        soundPlayer = null;
    }
}
