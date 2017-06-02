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
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Apple;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;
import com.hemantithide.borisendesjaak.GameObjects.Dragon;
import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.GameObjects.Rock;
import com.hemantithide.borisendesjaak.GameObjects.Sheep;

import java.util.HashSet;
import java.util.LinkedList;

import static com.hemantithide.borisendesjaak.GameSurfaceView.GameState.DRAGON;
import static com.hemantithide.borisendesjaak.GameSurfaceView.GameState.END_GAME;
import static com.hemantithide.borisendesjaak.GameSurfaceView.GameState.ROCKS;
import static com.hemantithide.borisendesjaak.GameSurfaceView.GameState.START_GAME;

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
    public Kinker kinker;

    public LinkedList<Integer> laneXValues;
    public LinkedList<Integer> laneYValues;

    public LinkedList<Integer> primaryRocks;
    private LinkedList<Integer> secondaryRocks;
    private int spawnWaveCount;

    public LinkedList<Integer> appleSequence;
    private int applesSpawned;

    public GameActivity activity;
    public LinkedList<GameObject> gameObjects;

    private SpriteLibrary spriteLibrary;

    public int frameCount = -60;
    private TextView frameCounter;
    public Canvas canvas;
    public DisplayMetrics metrics;

    public enum GameState { START_GAME, ROCKS, DRAGON, END_GAME, WIN_WINDOW, LOSE_WINDOW }
    public HashSet<GameState> activeStates = new HashSet<>();

    private int dragonPresentTimer;
    private int dragonAbsentTimer = 100;

    private AftermathWindow aftermathWindow;

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

                initSpriteLibrary();
                initGameSpeed();
                initThread();
                initBackgroundLoop();
                setLanePositions();
                initGame();
//                animateBackground();
                initSequenceSeeds();

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

    private void initSpriteLibrary() {
        spriteLibrary = new SpriteLibrary(this);
    }

    private void initGameSpeed() {
        initGameSpeed = metrics.heightPixels / 150;
        gameSpeed = metrics.heightPixels / 150;
        speedMultiplier = 1.0;
    }

    private void initGame() {

        activeStates.add(START_GAME);

        player = new Sheep(this, 1);
//        opponent = new Sheep(this, 2);
        dragon = new Dragon(this);
    }

    private void initThread() {

        // init actual game
        thread = new GameThread(this);
        thread.running(true);
        thread.start();
    }

    private void initSequenceSeeds() {
        primaryRocks = new LinkedList<>();
        secondaryRocks = new LinkedList<>();
        appleSequence = new LinkedList<>();

        for(int i = 0; i < 9999; i++) {
            int randomNumberA = (int)Math.floor(Math.random() * 5);

            primaryRocks.add(randomNumberA);

            int randomNumberB;
            do randomNumberB = (int)Math.floor(Math.random() * 5);
            while(randomNumberA == randomNumberB);

            secondaryRocks.add(randomNumberB);

            int randomNumberC;
            do randomNumberC = (int)Math.floor(Math.random() * 5);
            while(randomNumberC == randomNumberA);

            appleSequence.add(randomNumberC);
        }

        Log.e("Primary   rock sequence", primaryRocks + "");
        Log.e("Secondary rock sequence", secondaryRocks + "");
        Log.e("Apple          sequence", appleSequence + "");
    }

    protected void updateCanvas(Canvas canvas) {

        for(Background b : backgroundBmps) {
            b.update();
            b.draw(canvas);
        }

        for(int i = 0; i < player.appleCounter; i++) {
            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.APPLE_SMALL), metrics.widthPixels * (0.05f + (0.025f * i)), metrics.heightPixels * 0.90f, null);
        }

        LinkedList<GameObject> toUpdate = new LinkedList<>(gameObjects);
        for(GameObject g : toUpdate) {
            g.update();
            g.draw(canvas);
        }

        if(aftermathWindow != null) {
            aftermathWindow.update();
            aftermathWindow.draw(canvas);
        }

        if(activeStates.contains(START_GAME)) {
            if (frameCount == 0) {
                activateState(ROCKS);
            }
        }

        if(!activeStates.contains(END_GAME))
            addFrameCount();

        int interval = (int)(90 / speedMultiplier);

        if(frameCount % interval == 0) {

            spawnWaveCount++;

            if(Math.random() < 0.1 && kinker == null) {
                kinker = new Kinker(this);
            } else {
                new Apple(this, appleSequence.get(spawnWaveCount));
            }

            if (activeStates.contains(ROCKS) && frameCount % interval == 0) {
                new Rock(this, primaryRocks.get(spawnWaveCount));

                double secSpawnChance = 0.2 * speedMultiplier;
                if (Math.random() < (secSpawnChance > 0.6 ? 0.6 : secSpawnChance) && (frameCount % interval == interval / 2)) {
                    new Rock(this, secondaryRocks.get(spawnWaveCount));
                }
            }
        }

        if(activeStates.contains(DRAGON)) {
            if (dragonPresentTimer > 0)
                dragonPresentTimer--;

            if(dragonPresentTimer == 0)
                deactivateState(DRAGON);
        } else {
            if(dragonAbsentTimer > 0)
                dragonAbsentTimer--;

            if(dragonAbsentTimer == 0)
                activateState(DRAGON);
        }

        if(activeStates.contains(END_GAME)) {
            if(speedMultiplier > 0) {
                speedMultiplier -= 0.01 + (speedMultiplier / 200);
                gameSpeed = (long)(initGameSpeed * speedMultiplier);
        }

            if(speedMultiplier <= 0 && !dragon.flyingOut) {
                speedMultiplier = 0;
                dragon.flyOut(player);
            }
        }
    }

    public void activateState(GameState state) {

        switch(state) {
            case DRAGON:
                dragonPresentTimer = 500;
                dragon.setState(Dragon.State.PRESENT);
                break;
            case END_GAME:
                deactivateState(START_GAME);
                deactivateState(ROCKS);

                if(dragon.state != Dragon.State.PRESENT)
                    dragon.setState(Dragon.State.PRESENT);

                dragonPresentTimer = 1000;
            case LOSE_WINDOW:
                aftermathWindow = new AftermathWindow(this);
        }
        activeStates.add(state);
    }

    private void deactivateState(GameState state) {
        switch(state) {
            case DRAGON:
                dragonAbsentTimer = 1000;
                dragon.setState(Dragon.State.ABSENT);
                break;
        }
        activeStates.remove(state);
    }

    private void initBackgroundLoop() {

        backgroundBmps.add(new Background(this, 0));
        backgroundBmps.add(new Background(this, -metrics.heightPixels));
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