package com.hemantithide.borisendesjaak.Engine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.hemantithide.borisendesjaak.GameObjects.Opponent;
import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.Network.Server;
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

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.DRAGON;
import static com.hemantithide.borisendesjaak.Engine.GameSurfaceView.GameState.END_GAME;
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

    public GameThread gameThread;
    public VisualThread visualThread;

    public LinkedList<Background> backgroundBmps;

    public Sheep player;
    public Opponent opponent;

    private boolean multiplierActive;

    public static String notification;
    private boolean milestoneReached;
    public static int notificationAlpha;
    public static int notificationTimer;
    private LinkedList<String> randomCompliments = new LinkedList<>();

    public static HashSet<String> notificationChecklist = new HashSet<>();

    public boolean isMultiplierActive() {
        return multiplierActive;
    }

    public void setMultiplierActive(boolean multiplierActive) {
        this.multiplierActive = multiplierActive;
    }

    public Dragon dragon;
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

    public Seed seed;
    private SpriteLibrary spriteLibrary;

    public int updateCounter = GameConstants.INIT_UPDATE_COUNTER_VALUE;

    public int distanceCount;
    private TextView distanceCounter;

    public Canvas canvas;
    public DisplayMetrics metrics;

    public boolean gamePaused;

    public boolean playerReady;
    public boolean otherPlayerReady;
    public boolean bothPlayersReady;

    private boolean tutBlink = true;

    public void pauseGame(boolean pause) {
        gamePaused = pause;

        if (GameActivity.IS_MULTIPLAYER) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (GameActivity.IS_CLIENT) {
                            if (gamePaused) {
                                Client.out.writeUTF("pause");
                            } else {
                                Client.out.writeUTF("resume");
                            }
                        } else if (GameActivity.IS_SERVER) {
                            if (gamePaused) {
                                Server.out.writeUTF("pause");
                            } else {
                                Server.out.writeUTF("resume");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public enum GameState {START_GAME, ROCKS, DRAGON, END_GAME, WIN_WINDOW, LOSE_WINDOW, REWARDS}

    public HashSet<GameState> activeStates = new HashSet<>();

    public int dragonPresentTimer;
    private int dragonAbsentTimer = GameConstants.INIT_DRAGON_ABSENT_TIMER;

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

                initGameSpeed();
                initSpriteLibrary();
                initThreads();
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


    private void initSpriteLibrary() {
        spriteLibrary = new SpriteLibrary(this);
    }

    private void initGameSpeed() {
//        initGameSpeed = metrics.heightPixels / 150;
        initGameSpeed = GameConstants.GAME_SPEED;
        gameSpeed = GameConstants.GAME_SPEED;
        speedMultiplier = 1.0;
    }

    private void initGame() {

        player = new Sheep(this, 1);
//        opponent = new Sheep(this, 2);
        dragon = new Dragon(this);

        if (GameActivity.IS_MULTIPLAYER)
            opponent = new Opponent(this);

        randomCompliments.add("Nice!");
        randomCompliments.add("Wow!");
        randomCompliments.add("Amazing!");
    }

    private void initThreads() {

        gameThread = new GameThread(this);
        gameThread.running(true);
        gameThread.start();

        visualThread = new VisualThread(this);
        visualThread.running(true);
        visualThread.start();
    }

    protected void updateCanvas(Canvas canvas) {

        for (Background b : backgroundBmps)
            b.draw(canvas);

        drawAllObjects();

        if (aftermathWindow != null)
            aftermathWindow.draw(canvas);

        if (gamePaused) {
            drawPauseText();
        }

        if(notification != null && !gamePaused) {
            drawMilestone(canvas);
        }

        if (updateCounter > 0 && updateCounter < 300)
            drawTutorial(canvas);

        // draw counters
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");
        paint.setTypeface(tf);
        paint.setTextSize(36);
        paint.setTextAlign(Paint.Align.LEFT);

        if (player.health < 3) {
            drawAppleCounter(paint);
        }

        drawDucatCounter(paint);
    }

    public void drawMilestone(Canvas canvas){
        canvas.drawText(notification, metrics.widthPixels / 2, metrics.heightPixels / 2, getFontPaint());
    }

    public void milestoneFadeOut(Canvas canvas){

    }

    public Paint getFontPaint(){
        Paint paint = new Paint();
        paint.setTypeface(MainActivity.tf);
        paint.setTextSize(36);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setAlpha(notificationAlpha);

        return paint;
    }

    private void drawTutorial(Canvas canvas) {

        Bitmap sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.TUTORIAL);

        if (tutBlink) {
            canvas.drawBitmap(
                    sprite,
                    player.posX - sprite.getWidth() / 2,
                    player.posY - sprite.getHeight() / 2,
                    null);
        }
    }

//=======
    private synchronized void drawAllObjects() {
        LinkedList<GameObject> toDraw = new LinkedList<>(gameObjects);
        Collections.sort(toDraw, DrawPriorityComparator);

        for (GameObject g : toDraw)
            g.draw(canvas);
//>>>>>>> 256748c45466708c4d6785cf794d0c509bedf8f5
    }

    private void drawPauseText() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setTypeface(MainActivity.tf);

        paint.setTextSize(72);
        canvas.drawText(getResources().getString(R.string.game_paused), canvas.getWidth() / 2, canvas.getHeight() / 2, paint);

        paint.setTextSize(36);
        canvas.drawText(getResources().getString(R.string.game_paused_description), canvas.getWidth() / 2, (int) (canvas.getHeight() * 0.55), paint);
    }

    private void drawAppleCounter(Paint paint) {

        canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.APPLE_ICON), canvas.getWidth() * 0.05f, canvas.getHeight() * 0.9f, null);
        canvas.drawText(player.appleCounter + "/" + player.requiredApples, canvas.getWidth() * 0.1f, canvas.getHeight() * 0.925f, paint);
    }

    private void drawDucatCounter(Paint paint) {
        paint.setAlpha(255);
        canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), canvas.getWidth() * 0.05f, canvas.getHeight() * 0.95f, null);
        canvas.drawText(player.ducatCounter + "", canvas.getWidth() * 0.1f, canvas.getHeight() * 0.975f, paint);
    }

    public void update() {

        LinkedList<Background> bgsToUpdate = new LinkedList<>(backgroundBmps);
        for (Background b : bgsToUpdate)
            b.update();

        LinkedList<GameObject> toUpdate = new LinkedList<>(gameObjects);
        for (GameObject g : toUpdate)
            g.update();

        if (updateCounter > 0 && !activeStates.contains(ROCKS)) {
            activateState(ROCKS);
            deactivateState(START_GAME);
        }

        if(updateCounter > 0 && updateCounter < 300 && updateCounter % 30 == 0)
            tutBlink = !tutBlink;

        if (bothPlayersReady) {

            if (!activeStates.contains(END_GAME))
                increaseUpdateCounter();

            if (!activeStates.contains(END_GAME) && !activeStates.contains(START_GAME))
                spawnObjects();

            if (activeStates.contains(DRAGON)) {
                if (dragonPresentTimer > 0)
                    dragonPresentTimer--;

                if (dragonPresentTimer == 0)
                    deactivateState(DRAGON);
            } else {
                if (dragonAbsentTimer > 0 && updateCounter > 0)
                    dragonAbsentTimer--;

                if (dragonAbsentTimer == 0)
                    activateState(DRAGON);
            }
        }

        if (activeStates.contains(END_GAME)) {
            if (speedMultiplier > 0) {
                speedMultiplier -= 0.01 + (speedMultiplier / 200);
                gameSpeed = (long) (initGameSpeed * speedMultiplier);
            }

            if (speedMultiplier <= 0 && !dragon.flyingOut) {
                speedMultiplier = 0;
                dragon.flyOut(player);
            }
        }

        if (aftermathWindow != null)
            aftermathWindow.update();
    }

    private void spawnObjects() {

        int interval = (int) (GameConstants.WAVE_SPAWN_INTERVAL / speedMultiplier);

        if (updateCounter % interval == 0) {
            spawnWaveCount++;

            if (spawnWaveCount > 1000)
                spawnWaveCount = 0;

            if (player != null && player.health < 3)
                new Apple(this, seed.appleSeq.get(spawnWaveCount));

            if (!(dragon.visitCounter > 2 && dragonAbsentTimer > GameConstants.DRAGON_ABSENT_TIMER * 0.95) && activeStates.contains(ROCKS)) {
                new Rock(this, seed.rockSeqA.get(spawnWaveCount));
            }

            if (seed.spawnChanceKinker.get(spawnWaveCount) < 0.05 && kinker == null) {
                kinker = new Kinker(this, seed.kinkerSeq.get(spawnWaveCount));
            }
        }

        if (updateCounter % interval == interval / 2) {

            if (spawnWaveCount % 4 == 0)
                new Ducat(this, seed.ducatSeq.get(spawnWaveCount));

            double secSpawnChance = 0.2 * speedMultiplier;
            if (!(dragon.visitCounter > 2 && dragonAbsentTimer > GameConstants.DRAGON_ABSENT_TIMER * 0.95)
                    && !activeStates.contains(DRAGON) && seed.spawnChanceRockB.get(spawnWaveCount) < secSpawnChance) {
                new Rock(this, seed.rockSeqB.get(spawnWaveCount));
            }
        }
    }

    public void activateState(GameState state) {

        switch (state) {
            case DRAGON:
                dragonPresentTimer = GameConstants.DRAGON_PRESENT_TIMER;
                dragon.setState(Dragon.State.PRESENT);
                dragon.increaseVisitCounter();

                GameNotificationManager.showNotification(GameNotificationManager.Notification.BORIS, true);
                break;
            case END_GAME:
                deactivateState(START_GAME);
                deactivateState(ROCKS);

                if (dragon.state != Dragon.State.PRESENT)
                    dragon.setState(Dragon.State.PRESENT);

                dragonPresentTimer = GameConstants.DRAGON_PRESENT_TIMER;
            case LOSE_WINDOW:
                aftermathWindow = new AftermathWindow(this);
        }

        activeStates.add(state);
    }

    private void deactivateState(GameState state) {
        switch (state) {
            case DRAGON:
                dragonAbsentTimer = GameConstants.DRAGON_ABSENT_TIMER;
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

        if (!gamePaused)
            if (Boolean.toString(player.isAlive()).equals("true")) {
                player.moveLeft();
                new Thread(new Write()).start();
            }
    }

    public void onSwipeRight() {

        if (!gamePaused)
            if (Boolean.toString(player.isAlive()).equals("true")) {
                player.moveRight();
                new Thread(new Write()).start();
            }
    }

    public void onSwipeDown() {

        if (!gamePaused)
            if (Boolean.toString(player.isAlive()).equals("true")) {
                player.moveDown();
                new Thread(new Write()).start();
            }
    }

    public void onSwipeUp() {

        if (!gamePaused)
            if (Boolean.toString(player.isAlive()).equals("true")) {
                player.moveUp();
                new Thread(new Write()).start();
            }
    }

    class Write implements Runnable {

        @Override
        public void run() {
            try {
//                String write = player.targetX + "-" + player.targetY;
                String write = player.horizLaneID + "-" + player.vertiLaneID;

                if (GameActivity.IS_SERVER)
                    Server.out.writeUTF(write);
                else if (GameActivity.IS_CLIENT) {
                    Client.out.writeUTF(write);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void increaseUpdateCounter() {

        updateCounter++;
        distanceCount += speedMultiplier;

        if(Math.floor(distanceCount/10) >= 500 && Math.floor(distanceCount/10) % 500 == 0 && !milestoneReached){
            notification = randomCompliments.get((int)(Math.random() * randomCompliments.size())) + "! You've reached " + (int)Math.floor(distanceCount/10) + "m!";
            notificationAlpha = 255;
            notificationTimer = 180;

            milestoneReached = true;
        }

        if (notificationTimer > 0) {
            notificationTimer--;

            if(notificationTimer < 60)
                notificationAlpha = (int)(notificationTimer * 4.25);

            if(notificationTimer == 0) {
                notification = null;
                milestoneReached = false;
            }
        }

        if (updateCounter > 0 && updateCounter % GameConstants.GAME_SPEED_INCREASE_INTERVAL == 0 && speedMultiplier < 4) {
            speedMultiplier += 0.05;
            gameSpeed = (long) (initGameSpeed * speedMultiplier);
        }

        if (activity.distanceCounter != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    distanceCounter.setVisibility(VISIBLE);
                    distanceCounter.setText(String.valueOf(distanceCount / 10) + "m");
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    distanceCounter.setVisibility(INVISIBLE);
                }
            });
        }
    }

    public void setDistanceCounter(TextView distanceCounter) {
        this.distanceCounter = distanceCounter;
    }

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }

    private void setLanePositions() {
        laneXValues = new LinkedList<>();

        for (int i = 0; i < 5; i++)
            laneXValues.add(((metrics.widthPixels / 5) * i) + (metrics.widthPixels / 10));

        laneYValues = new LinkedList<>();

        for (int i = 0; i < 7; i++)
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

        gameThread.interrupt();
        visualThread.interrupt();

        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtra("DUCATS", ducats);
        i.putExtra("WON", false);
        i.putExtra("C_APPLES", player.applesCollected);
        i.putExtra("C_DUCATS", player.ducatsCollected);
        i.putExtra("C_KINKERS", player.kinkersCollected);
        i.putExtra("DISTANCE", updateCounter / 10);
        activity.startActivity(i);
    }
}