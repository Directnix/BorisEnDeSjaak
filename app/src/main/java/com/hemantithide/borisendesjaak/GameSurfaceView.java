package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.GameObjects.Rock;
import com.hemantithide.borisendesjaak.GameObjects.Sheep;

import java.util.LinkedList;

/**
 * Created by Daniel on 23/05/2017.
 */

public class GameSurfaceView extends SurfaceView {

    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;

    private long gameSpeed = 7000L;

    GameThread thread;

    private Sheep player;
    private Sheep opponent;

    private LinkedList<Integer> lanePositionValues;

    public LinkedList<Integer> primaryRocks;
    private LinkedList<Integer> secondaryRocks;
    private int rocksSpawned;

    private ImageView playerSprite;
    private ImageView opponentSprite;

    private ImageView backgroundGrassOne;
    private ImageView backgroundGrassTwo;

    private GameActivity activity;
    public LinkedList<GameObject> gameObjects;

    private int frameCount;
    private TextView frameCounter;
    public Canvas canvas;

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
                canvas = surfaceHolder.lockCanvas();

                gameObjects = new LinkedList<>();

                initThread();
                initPlayers();
                animateBackground();
                initRockSequence();

//                updateCanvas(canvas);

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

    private void initPlayers() {

        player = new Sheep(this, 1);
        opponent = new Sheep(this, 2);
    }

    private void initThread() {

        // init actual game
        thread = new GameThread(this);
        thread.running(true);
        thread.start();
    }

    private void animateBackground() {

        //creating variables
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f ,1.0f);

        //setting animator up
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(gameSpeed);

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

    public void snapToLane(int laneID) {
        playerSprite.setTranslationX(lanePositionValues.get(laneID) - 16);
    }

    protected void updateCanvas(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(100, 100, 50, paint);

        for(GameObject g : gameObjects) {
            g.update();
            g.draw(canvas);
        }

        if(frameCount % 60 == 0) {
            spawnRock();
        }

        addFrameCount();

    }

    private void spawnRock() {

        rocksSpawned++;
        new Rock(this, rocksSpawned);

        Log.e("Rock spawned in lane", primaryRocks.get(rocksSpawned) + "");
    }

    public void onSwipeLeft() {

        if(Boolean.toString(player.isAlive()).equals("true")) {
            player.moveLeft();
            Log.e("links", " Links");
        }
    }

    public void onSwipeRight() {

        if(Boolean.toString(player.isAlive()).equals("true")) {
            player.moveRight();
            Log.e("rechts", " Rechtts");
        }
    }

    public void setLanePositionValues(LinkedList<Integer> lanePositionValues) {
        this.lanePositionValues = lanePositionValues;
    }

    public void setSpriteViews(ImageView playerSprite, ImageView opponentSprite) {
        this.playerSprite = playerSprite;
        this.opponentSprite = opponentSprite;
    }

    public void setBackgroundImageView(ImageView backgroundGrassOne, ImageView backgroundGrassTwo) {
        this.backgroundGrassOne = backgroundGrassOne;
        this.backgroundGrassTwo = backgroundGrassTwo;
    }

    public void addFrameCount() {
        if(activity.frameCounter != null) {
            frameCount++;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    frameCounter.setText(String.valueOf(frameCount));
                }
            });

            if(frameCount % 60 == 0) {

            }
        }
    }

    public void setFrameCounter(TextView frameCounter) {
        this.frameCounter = frameCounter;
    }

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }

    public LinkedList<Integer> getLanePositionValues() {
        return lanePositionValues;
    }
}