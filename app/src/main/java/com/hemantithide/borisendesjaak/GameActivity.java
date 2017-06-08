package com.hemantithide.borisendesjaak;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.Seed;
import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.Network.Server;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;






/*      TODO
        - pause on leaving the app
        - mockup settings menu
        - uitwerking settings menu
        - hide notifications during game session
        - moeilijkheid baseren op door de speler ingevoerde leeftijd (?)
        - bij eerste opstart: de speler keuze geven uit 3 à 5 randomly generated usernames
        - twee main activity frames:
          - 1. kies leeftijd/geslacht (voor de eerste opstart; indien er nog geen User-object opgeslagen is)
          - 2. kies uit 3-5 randomly generated usernames
          - 3. shop (alleen een paar buttons eigenlijk)
        - language instelling overbodig? (aangezien de app de taal aanpast aan die van de telefoon)
        - een betere manier om sprites in de lanes te centreren (gaat nu hardcoded per klasse)
*/


public class GameActivity extends AppCompatActivity {
    public String username;

    public enum Sound {
        ROCK_HIT,
        SWIPE,
        WOOSH,
        AYO_WHADDUP,
        POWERUP,
        POWERUP_LOOP,
        KINKER,
        KINKER_2,
        BORIS_CHARGE,
        FIREBALL,
        FIRE_ON_ROCK,
        SHEEP_SCREECH,
        BARF,
        DUCAT,
        WOW
    }

    private GameSurfaceView surfaceView;
    private ImageView transparentView;

    private enum ActiveFrame {PREGAME, PAUSE, AFTERMATH}

    private ActiveFrame activeFrame = ActiveFrame.PREGAME;

    private FrameLayout pregameButtonFrame, pauseButtonFrame, aftermathButtonFrame;
    private Button buttonMultiplier, buttonRematch, buttonQuit, buttonLeave;
    private ImageButton buttonMute;

    public TextView distanceCounter;

    MediaPlayer mediaPlayer;
    MediaPlayer soundPlayer;

    public static boolean IS_MULTIPLAYER = false;
    public static boolean IS_SERVER = false;
    public static boolean IS_CLIENT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        surfaceView = (GameSurfaceView) findViewById(R.id.game_srfcvw);
//        surfaceView.setBackgroundImageView(backgroundGrassOne, backgroundGrassTwo);

        distanceCounter = (TextView) findViewById(R.id.game_txtvw_counter);
        distanceCounter.setTypeface(MainActivity.tf);
        surfaceView.setDistanceCounter(distanceCounter);

        surfaceView.setActivity(this);
        surfaceView.seed = (Seed) getIntent().getSerializableExtra("SEED");

        initFrames();

        // give metrics to surface view
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        surfaceView.setMetrics(metrics);

        // Swipe
        transparentView = (ImageView) findViewById(R.id.game_imgvw_transparent);
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


        if (getIntent().getExtras().getBoolean("MULTIPLAYER")) {
            IS_MULTIPLAYER = true;

            if(getIntent().getExtras().getBoolean("SERVER")) {
                IS_SERVER = true;
            }

            if(getIntent().getExtras().getBoolean("CLIENT")) {
                IS_CLIENT = true;
            }

            new Thread(new Read()).start();
            new Thread(new Write()).start();
        }
    }

    private void initFrames() {
        buttonMultiplier = (Button) findViewById(R.id.game_btn_multiplier);
        buttonMultiplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.user.consumeMultiplier();
                surfaceView.setMultiplierActive(true);

                playSound(Sound.WOW);
                MainActivity.animateButton(getApplicationContext(), buttonMultiplier, R.anim.pop_out);

                animate(pregameButtonFrame, false, 1);
                activeFrame = null;
            }
        });

        buttonMute = (ImageButton) findViewById(R.id.game_btn_mute);
        buttonMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.musicPlaying = !MainActivity.musicPlaying;

                if (MainActivity.musicPlaying) {
                    buttonMute.setImageResource(R.drawable.button_play);
                }
                if (!MainActivity.musicPlaying) {
                    buttonMute.setImageResource(R.drawable.button_mute);
                }

                MainActivity.animateButton(getApplicationContext(), buttonMute, R.anim.button_clicked);
            }
        });

        buttonLeave = (Button) findViewById(R.id.game_btn_exit_early);
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                surfaceView.activateState(GameSurfaceView.GameState.END_GAME);

                animate(pauseButtonFrame, false, 0);
                activeFrame = null;
            }
        });

        buttonRematch = (Button) findViewById(R.id.game_btn_rematch);
        buttonRematch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(i);
            }
        });

        buttonQuit = (Button) findViewById(R.id.game_btn_quit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame();

                activeFrame = null;
            }
        });

        pregameButtonFrame = (FrameLayout) findViewById(R.id.game_fl_pregame);
        MainActivity.animateButton(getApplicationContext(), buttonMultiplier, R.anim.pop_in);
        if (MainActivity.user.multipliers > 0) {
            buttonMultiplier.setClickable(true);
            buttonMultiplier.setAlpha(1);
        } else {
            buttonMultiplier.setClickable(false);
            buttonMultiplier.setAlpha(0.25f);
        }

        pauseButtonFrame = (FrameLayout) findViewById(R.id.game_fl_pause);
        pauseButtonFrame.setVisibility(View.INVISIBLE);
        buttonMute.setClickable(false);
        buttonLeave.setClickable(false);

        aftermathButtonFrame = (FrameLayout) findViewById(R.id.game_fl_aftermath);
        aftermathButtonFrame.setVisibility(View.INVISIBLE);
        buttonRematch.setClickable(false);
        buttonQuit.setClickable(false);

        activeFrame = ActiveFrame.PREGAME;
    }

    public void hidePregameFrame() {
        if (activeFrame != null) {
            activeFrame = null;

            MainActivity.animateButton(getApplicationContext(), buttonMute, R.anim.pop_out);
//            animate(pregameButtonFrame, false, 1);
            buttonMultiplier.setClickable(false);
        }
    }

    public void showAftermathFrame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aftermathButtonFrame.setVisibility(View.VISIBLE);
            }
        });

        animate(aftermathButtonFrame, true, 0);

        buttonRematch.setClickable(true);
        buttonQuit.setClickable(true);

        activeFrame = ActiveFrame.AFTERMATH;
    }

    public void endGame() {
        surfaceView.gameThread.interrupt();
        surfaceView.visualThread.interrupt();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("DUCATS", surfaceView.aftermathWindow.totalDucats);
        i.putExtra("WON", false);
        i.putExtra("C_APPLES", surfaceView.player.applesCollected);
        i.putExtra("C_DUCATS", surfaceView.player.ducatsCollected);
        i.putExtra("C_KINKERS", surfaceView.player.kinkersCollected);
        i.putExtra("DISTANCE", surfaceView.updateCounter / 10);
        startActivity(i);

        surfaceView.aftermathWindow = null;
    }

    private void animate(FrameLayout fl, boolean load, int dir) {

        final FrameLayout fl2 = fl;
        final boolean load2 = load;
        final int dir2 = dir;

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Animation aOut;
                Animation aIn;

                if (dir2 == 0) {
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

                if (load2) {
                    fl2.clearAnimation();
                    fl2.startAnimation(aIn);
                } else {
                    fl2.clearAnimation();
                    fl2.startAnimation(aOut);
                }

//        currentFrame = to;
                fl2.bringToFront();
            }
        });
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

    @Override
    public void onBackPressed() {

        if (activeFrame != ActiveFrame.PREGAME) {
            if (!surfaceView.activeStates.contains(GameSurfaceView.GameState.END_GAME) && (activeFrame == null || activeFrame == ActiveFrame.PREGAME)) {
                surfaceView.pauseGame(true);

                pauseButtonFrame.setVisibility(View.VISIBLE);
                animate(pauseButtonFrame, true, 0);

                buttonMute.setClickable(true);
                buttonLeave.setClickable(true);

                if (MainActivity.musicPlaying)
                    mediaPlayer.pause();

                soundPlayer.pause();

                activeFrame = ActiveFrame.PAUSE;
            } else {
                switch (activeFrame) {
                    case PAUSE:
                        surfaceView.pauseGame(false);

                        pauseButtonFrame.setVisibility(View.INVISIBLE);
                        animate(pauseButtonFrame, false, 0);

                        buttonMute.setClickable(false);
                        buttonLeave.setClickable(false);

                        if (MainActivity.musicPlaying)
                            mediaPlayer.start();
                        soundPlayer.start();

                        activeFrame = null;
                        break;
                    case AFTERMATH:
                        close();
                        break;
                }
            }
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        surfaceView.canvas.save();
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        surfaceView.canvas.restore();
//    }

    public void close() {
        super.onBackPressed();
        MainActivity.user.save(getApplicationContext());
    }

    public void playSound(Sound sound) {

        int input = R.raw.swipe;

        switch (sound) {
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
            case KINKER_2:
                input = R.raw.kinker_2;
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
            case DUCAT:
                input = R.raw.ducat;
                break;
            case BARF:
                input = R.raw.sheep_barf;
                break;
            case WOW:
                input = R.raw.wow;
                break;
        }

        soundPlayer = MediaPlayer.create(getApplicationContext(), input);
        soundPlayer.start();

        try {
            if (soundPlayer.isPlaying()) {
                soundPlayer.seekTo(0);

                soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                    }
                });
            }
        } catch (NullPointerException ignored) {
        }
    }


    class Read implements Runnable {
        @Override
        public void run() {
            while(true){
                try {
                    if(surfaceView != null) {
                        if (surfaceView.opponent != null) {
                            String result = "";
                            if (IS_SERVER)
                                result = Server.in.readUTF();
                            else if (IS_CLIENT) {
                                result = Client.in.readUTF();
                                Log.i("Client read", result);
                            }
                            if(!result.isEmpty()) {
                                try {
                                    surfaceView.opponent.posX = Integer.parseInt(result.split("-")[0]);
                                    surfaceView.opponent.posY = Integer.parseInt(result.split("-")[1]);
                                } catch (Exception ex) {
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Write implements Runnable {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(surfaceView != null) {
                        if(surfaceView.player != null) {
                            try {
                                if (IS_SERVER)
                                    Server.out.writeUTF(surfaceView.player.posX + "-" + surfaceView.player.posY);
                                else if (IS_CLIENT) {
                                    Client.out.writeUTF(surfaceView.player.posX + "-" + surfaceView.player.posY);
                                    Log.e("Client write", surfaceView.player.posX + "-" + surfaceView.player.posY);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, 0, 10);
        }
    }
}
