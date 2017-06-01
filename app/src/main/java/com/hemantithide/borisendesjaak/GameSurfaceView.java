package com.hemantithide.borisendesjaak;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.GameObjects.Background;
import com.hemantithide.borisendesjaak.GameObjects.Dragon;
import com.hemantithide.borisendesjaak.GameObjects.Fireball;
import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.GameObjects.Rock;
import com.hemantithide.borisendesjaak.GameObjects.Sheep;

import java.util.LinkedList;

/**
 * Created by Daniel on 23/05/2017.
 */

public class GameSurfaceView extends SurfaceView {

    private SurfaceHolder surfaceHolder;

    public long initGameSpeed;
    public double speedMultiplier;
    public long gameSpeed;

    GameThread thread;

    public LinkedList<Background> backgroundBmps;

    public Sheep player;
    private Sheep opponent;

    private Dragon dragon;

    public LinkedList<Integer> laneXValues;
    public LinkedList<Integer> laneYValues;

    public LinkedList<Integer> primaryRocks;
    private LinkedList<Integer> secondaryRocks;
    private int rocksSpawned;

    public GameActivity activity;
    public LinkedList<GameObject> gameObjects;

    public int frameCount = -60;
    private TextView frameCounter;
    public Canvas canvas;
    public DisplayMetrics metrics;

<<<<<<< HEAD
    private enum GameState { START_GAME, ROCKS, DRAGON, END_GAME, }
    private GameState gameState = GameState.START_GAME;








=======
>>>>>>> sprites



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

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                canvas = surfaceHolder.lockCanvas();

                gameObjects = new LinkedList<>();
                backgroundBmps = new LinkedList<>();

                initGameSpeed();
                initThread();
                initBackgroundLoop();
                setLanePositions();
                initPlayers();
//                animateBackground();
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

    private void initGameSpeed() {
        initGameSpeed = metrics.heightPixels / 150;
        gameSpeed = metrics.heightPixels / 150;
        speedMultiplier = 1.0;
    }

    private void initPlayers() {

        player = new Sheep(this, 1);
//        opponent = new Sheep(this, 2);
    }

    private void initThread() {

        // init actual game
        thread = new GameThread(this);
        thread.running(true);
        thread.start();
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

    protected void updateCanvas(Canvas canvas) {

        for(Background b : backgroundBmps) {
            b.update();
            b.draw(canvas);
        }

        LinkedList<GameObject> toUpdate = new LinkedList<>(gameObjects);
        for(GameObject g : toUpdate) {
            g.update();
            g.draw(canvas);
        }

        switch(gameState) {
            case START_GAME:
                if(frameCount == 0) {
                    gameState = GameState.DRAGON;
                }
                break;
            case ROCKS:
                if(frameCount % (int) (90 / speedMultiplier) == 0) {
                    spawnRock(primaryRocks);

                    double secSpawnChance = 0.2 * speedMultiplier;

                    if (Math.random() < (secSpawnChance > 0.6 ? 0.6 : secSpawnChance)) ;
                    spawnRock(secondaryRocks);
                }
                break;
            case DRAGON:
                if(dragon == null)
                    dragon = new Dragon(this);

                if(frameCount % (int) (90 / speedMultiplier) == 0) {
//                    spawnFireball();

                    double secSpawnChance = 0.2 * speedMultiplier;

                    if (Math.random() < (secSpawnChance > 0.6 ? 0.6 : secSpawnChance)) ;
                    spawnRock(secondaryRocks);
                }
                break;
            case END_GAME:
                break;
        }

        addFrameCount();
    }

    private void initBackgroundLoop() {

        backgroundBmps.add(new Background(this, 0));
        backgroundBmps.add(new Background(this, -metrics.heightPixels));
    }

    private void spawnFireball() {
        int randomNumber = (int)Math.floor(Math.random() * 5);

        new Fireball(this, randomNumber);
    }

    private void spawnRock(LinkedList<Integer> rockSequence) {

        rocksSpawned++;
        new Rock(this, rocksSpawned);

        Log.e("Amount of objects", gameObjects.size() + "");
    }

    public void onSwipeLeft() {

        if(Boolean.toString(player.isAlive()).equals("true")) {
            player.moveLeft();
        }
    }

    public void onSwipeRight() {

        if(Boolean.toString(player.isAlive()).equals("true")) {
            player.moveRight();
        }
    }

    public void onSwipeDown() {

        if(Boolean.toString(player.isAlive()).equals("true")) {
            player.moveDown();
        }
    }

    public void onSwipeUp() {

        if(Boolean.toString(player.isAlive()).equals("true")) {
            player.moveUp();
        }
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
        }

        if(frameCount % 100 == 0) {
            speedMultiplier += 0.05;
            gameSpeed = (long)(initGameSpeed * speedMultiplier);
        }
    }

    public void setFrameCounter(TextView frameCounter) {
        this.frameCounter = frameCounter;
    }

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }

    private void setLanePositions() {
        laneXValues = new LinkedList<>();

        for(int i = 0; i < 5; i++)
            laneXValues.add(((metrics.widthPixels / 5) * i));

        laneYValues = new LinkedList<>();

        for(int i = 0; i < 7; i++)
            laneYValues.add(((metrics.heightPixels / 7) * i));
    }
}