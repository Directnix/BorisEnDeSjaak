package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Timer;

public class GameActivity extends AppCompatActivity
{
    private long speed = 6500L;
    private ImageView transparentView;

    private Sheep player;
    private ImageView playerSprite;

    private Sheep opponent;
    private ImageView opponentSprite;

    private GameThread thread;
    private SurfaceView surfaceView;

    private LinkedList<Integer> lanePositionValues;

    private LinkedList<Integer> primaryRocks;
    private LinkedList<Integer> secondaryRocks;

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

        //sheep initialization
        player = new Sheep(this, 1);
        opponent = new Sheep(this, 2);
        playerSprite = (ImageView)findViewById(R.id.game_imgvw_sheepA);
        playerSprite.setImageResource(player.getSprite());
        opponentSprite = (ImageView)findViewById(R.id.game_imgvw_sheepB);
        opponentSprite.setImageResource(opponent.getSprite());

        // Swipe
        transparentView = (ImageView)findViewById(R.id.game_imgvw_transparent);
        transparentView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

            @Override
            public void onSwipeLeft() {

                if(Boolean.toString(player.isAlive()).equals("true")) {
                    player.moveLeft();
                    Log.e("links", " Links");
                }
            }

            @Override
            public void onSwipeRight() {

                if(Boolean.toString(player.isAlive()).equals("true")) {
                    player.moveRight();
                    Log.e("rechts", " Rechtts");
                }
            }
        });

        setLanePositions();
        initRockSequence();

        // init actual game
        thread = new GameThread();
        thread.run();

        surfaceView = (SurfaceView)findViewById(R.id.game_srfcvw);
    }

    private void initRockSequence() {
        primaryRocks = new LinkedList<>();
        secondaryRocks = new LinkedList<>();

        for(int i = 0; i < 9999; i++) {
            int randomNumberA = (int)Math.floor(Math.random() * 5);

            primaryRocks.add(randomNumberA);

            int randomNumberB = 0;
            boolean isTheSameNumber = true;
            while(Boolean.toString(isTheSameNumber).equals("true")) {
                randomNumberB = (int) Math.floor(Math.random() * 5);

                if (randomNumberA == randomNumberB)
                    isTheSameNumber = true;
                else
                    isTheSameNumber = false;
            }

            secondaryRocks.add(randomNumberB);
        }

        Log.e("P", primaryRocks + "");
        Log.e("S", secondaryRocks + "");
    }

    private void setLanePositions() {
        lanePositionValues = new LinkedList<>();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        for(int i = 0; i < 5; i++)
            lanePositionValues.add(((metrics.widthPixels / 5) * i));
    }

    public void snapToLane(int laneID) {
        playerSprite.setTranslationX(lanePositionValues.get(laneID) - 16);
    }
}
