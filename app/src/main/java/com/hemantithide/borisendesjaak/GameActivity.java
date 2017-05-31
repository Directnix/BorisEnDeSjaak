package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Timer;

public class GameActivity extends AppCompatActivity
{
    private long speed = 6500L;

    private GameSurfaceView surfaceView;
    private ImageView transparentView;
    private ImageView playerSprite;
    private ImageView opponentSprite;

    private LinkedList<Integer> lanePositionValues;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
        setSpriteViews();

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

        setLanePositions();
    }

    private void setSpriteViews() {
        surfaceView.setSpriteViews(playerSprite, opponentSprite);
    }

    private void setLanePositions() {
        lanePositionValues = new LinkedList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        for(int i = 0; i < 5; i++)
            lanePositionValues.add(((metrics.widthPixels / 5) * i));

        surfaceView.setLanePositionValues(lanePositionValues);
    }
}
