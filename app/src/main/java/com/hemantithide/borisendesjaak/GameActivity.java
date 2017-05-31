package com.hemantithide.borisendesjaak;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity
{
    public enum Sound {ROCK_HIT, SWIPE }

    private long speed = 6500L;

    private GameSurfaceView surfaceView;
    private ImageView transparentView;
    private ImageView playerSprite;
    private ImageView opponentSprite;

    TextView frameCounter;

//    MediaPlayer mediaPlayer;
    MediaPlayer soundPlayer;

    private LinkedList<Integer> lanePositionValues;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // music
//        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ingamesong);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.stop();*

        // sound effects
        soundPlayer = MediaPlayer.create(getApplicationContext(), R.raw.swipe);
        soundPlayer.setLooping(false);

        //creating background imageviews
        final ImageView backgroundGrassOne = (ImageView)findViewById(R.id.game_imgvw_backgroundOne);
        final ImageView backgroundGrassTwo = (ImageView)findViewById(R.id.game_imgvw_backgroundTwo);

        //sheep sprite initialization
        playerSprite = (ImageView)findViewById(R.id.game_imgvw_sheepA);
        playerSprite.setImageResource(R.drawable.sheep_placeholder);
        opponentSprite = (ImageView)findViewById(R.id.game_imgvw_sheepB);
        opponentSprite.setImageResource(R.drawable.sheep_placeholder);

        // game surface view init
        surfaceView = (GameSurfaceView)findViewById(R.id.game_srfcvw);
        surfaceView.setBackgroundImageView(backgroundGrassOne, backgroundGrassTwo);

        frameCounter = (TextView)findViewById(R.id.game_txtvw_counter);
        surfaceView.setFrameCounter(frameCounter);

        setSpriteViews();

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
        });
    }

    private void setSpriteViews() {
        surfaceView.setSpriteViews(playerSprite, opponentSprite);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mediaPlayer.pause();
//    }

    public void playSound(Sound sound) {

        int input = R.raw.swipe;

        switch(sound) {
            case SWIPE:
                input = R.raw.swipe;
                break;
            case ROCK_HIT:
                input = R.raw.rock_hit;
                break;
        }

        soundPlayer = MediaPlayer.create(getApplicationContext(), input);
        soundPlayer.start();

        if(soundPlayer.isPlaying()) {
            soundPlayer.seekTo(0);
        }

//        soundPlayer.release();

        soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
            }
        });

        soundPlayer = null;
    }
}
