package com.hemantithide.borisendesjaak.Engine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.Visuals.Background;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Apple;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Ducat;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;
import com.hemantithide.borisendesjaak.GameObjects.Dragon;
import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.GameObjects.Rock;
import com.hemantithide.borisendesjaak.GameObjects.Sheep;
import com.hemantithide.borisendesjaak.MainActivity;
import com.hemantithide.borisendesjaak.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.DRAGON;
import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.END_GAME;
import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.REWARDS;
import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.ROCKS;
import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.START_GAME;

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
    public int spawnWaveCount;

    public LinkedList<Integer> appleSequence;
    private int applesSpawned;

    public GameActivity activity;
    public LinkedList<GameObject> gameObjects;

    public SeedStorage seedStorage;
    private SpriteLibrary spriteLibrary;

    public int frameCount = -60;
    private TextView frameCounter;
    public Canvas canvas;
    public DisplayMetrics metrics;

    public boolean gamePaused;

    public void pauseGame(boolean pause) {
        gamePaused = pause;
    }

    public enum GameState { START_GAME, ROCKS, DRAGON, END_GAME, WIN_WINDOW, LOSE_WINDOW, REWARDS }
    public HashSet<GameState> activeStates = new HashSet<>();

    public int dragonPresentTimer;
    private int dragonAbsentTimer = 100;

    public AftermathWindow aftermathWindow;

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
                generateSeed();
                initGameSpeed();
                initThread();
                initBackgroundLoop();
                setLanePositions();
                initGame();

                updateCanvas(canvas);

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

    private void generateSeed() {
        seedStorage = new SeedStorage();
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

    protected void updateCanvas(Canvas canvas) {

        for(Background b : backgroundBmps)
            b.draw(canvas);

        LinkedList<GameObject> toDraw = new LinkedList<>(gameObjects);
        Collections.sort(toDraw, DrawPriorityComparator);

        for(GameObject g : toDraw)
            g.draw(canvas);

        if(aftermathWindow != null)
            aftermathWindow.draw(canvas);

        if(gamePaused) {
            drawPauseText();
        }

        // draw counters
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");
        paint.setTypeface(tf);
        paint.setTextSize(36);
        paint.setTextAlign(Paint.Align.LEFT);

        drawAppleCounter(paint);
        drawDucatCounter(paint);
    }

    private void drawPauseText() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");
        paint.setTypeface(tf);

        paint.setTextSize(72);
        canvas.drawText(getResources().getString(R.string.game_paused), canvas.getWidth() / 2, canvas.getHeight() / 2, paint);

        paint.setTextSize(36);
        canvas.drawText(getResources().getString(R.string.game_paused_description), canvas.getWidth() / 2, (int)(canvas.getHeight() * 0.55), paint);
    }

    private void drawAppleCounter(Paint paint) {

        if (player.health == 3) {
            paint.setAlpha(63);
            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.APPLE_ICON), canvas.getWidth() * 0.05f, canvas.getHeight() * 0.9f, paint);
        } else {
            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.APPLE_ICON), canvas.getWidth() * 0.05f, canvas.getHeight() * 0.9f, null);
        }

        canvas.drawText(player.appleCounter + "/" + player.requiredApples, canvas.getWidth() * 0.1f, canvas.getHeight() * 0.925f, paint);
    }

    private void drawDucatCounter(Paint paint) {
        paint.setAlpha(255);
        canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), canvas.getWidth() * 0.05f, canvas.getHeight() * 0.95f, null);
        canvas.drawText(player.ducatCounter + "", canvas.getWidth() * 0.1f, canvas.getHeight() * 0.975f, paint);
    }

    public void update() {

        for(Background b : backgroundBmps)
            b.update();

        LinkedList<GameObject> toUpdate = new LinkedList<>(gameObjects);
        for(GameObject g : toUpdate)
            g.update();

        if(activeStates.contains(START_GAME))
            if (frameCount == 0)
                activateState(ROCKS);

        if(!activeStates.contains(END_GAME))
            addFrameCount();

        spawnObjects();

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

        if(activeStates.contains(REWARDS)) {

        }

        if(aftermathWindow != null)
            aftermathWindow.update();
    }

    private void spawnObjects() {

        int interval = (int)(90 / speedMultiplier);

        if(frameCount % 100 == 0) {

            if (seedStorage.spawnChanceKinker.get(spawnWaveCount) < 0.1 && kinker == null) {
                kinker = new Kinker(this, seedStorage.kinkerSeq.get(spawnWaveCount));
            }
        }

        if(frameCount % interval == 0) {
            spawnWaveCount++;

            new Apple(this, seedStorage.appleSeq.get(spawnWaveCount));

            if (activeStates.contains(ROCKS) && frameCount % interval == 0) {
                new Rock(this, seedStorage.rockSeqA.get(spawnWaveCount));
            }
        }

        if(frameCount % interval == interval / 2) {

            if(spawnWaveCount % 4 == 0)
               new Ducat(this, seedStorage.ducatSeq.get(spawnWaveCount));

            double secSpawnChance = 0.2 * speedMultiplier;
            if (!activeStates.contains(DRAGON) && seedStorage.spawnChanceRockB.get(spawnWaveCount) < secSpawnChance) {
                new Rock(this, seedStorage.rockSeqB.get(spawnWaveCount));
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
            case LOSE_WINDOW:
                aftermathWindow = null;
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

        if(!gamePaused) {
            if(Boolean.toString(player.isAlive()).equals("true"))
                player.moveDown();
        } else {
            activateState(GameSurfaceView.GameState.END_GAME);
            gamePaused = false;
//            thread.interrupt();
//            activity.close();
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
                    frameCounter.setText(String.valueOf(frameCount / 10) + "m");
                }
            });
        }

        if(frameCount % 100 == 0 && speedMultiplier < 4) {
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

    public void calculateRewards() {
        aftermathWindow.showRewards();
    }

    public static Comparator<GameObject> DrawPriorityComparator = new Comparator<GameObject>() {

        @Override
        public int compare(GameObject obj1, GameObject obj2) {
            if (obj1.drawPriority > obj2.drawPriority)
                return -1;
            else if (obj1.drawPriority < obj2.drawPriority)
                return 1;
            else
                return 0;
        }
    };

    public void endGame(int ducats) {
        aftermathWindow = null;

        thread.interrupt();

        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtra("DUCATS", ducats);
        i.putExtra("WON", false);
        i.putExtra("C_APPLES", player.applesCollected);
        i.putExtra("C_DUCATS", player.ducatsCollected);
        i.putExtra("C_KINKERS", player.kinkersCollected);
        i.putExtra("DISTANCE", frameCount / 10);
        activity.startActivity(i);
    }
}