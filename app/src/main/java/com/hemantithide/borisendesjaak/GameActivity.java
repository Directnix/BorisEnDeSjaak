package com.hemantithide.borisendesjaak;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.Network.Server;

import java.io.IOException;
import java.util.LinkedList;






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


public class GameActivity extends AppCompatActivity implements Seed.SeedListener {
    public String username;
    private String opponentName;

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

    public enum ActiveFrame {PREGAME, PAUSE, AFTERMATH}

    public ActiveFrame activeFrame = ActiveFrame.PREGAME;

    private FrameLayout pregameButtonFrame, pauseButtonFrame, aftermathButtonFrame;
    private Button buttonMultiplier, buttonReady, buttonRematch, buttonQuit, buttonLeave;
    private ImageButton buttonMute;
    private ImageButton buttonMutesfx;

    public TextView distanceCounter;
    private TextView txtvwOtherNameHint;
    private TextView txtvwOtherName;
    private TextView txtvwWaiting;

    MediaPlayer mediaPlayer;
    MediaPlayer soundPlayer;


    public static boolean IS_MULTIPLAYER = false;
    public static boolean IS_SERVER = false;
    public static boolean IS_CLIENT = false;

    boolean opponentReady = false;


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
        System.out.println(" test: " + MainActivity.musicPlaying);
        mediaPlayer.start();
        mediaPlayer.pause();

        if (MainActivity.musicPlaying) {
        } else {
            System.out.println(" pauzeer: " + MainActivity.musicPlaying);
            mediaPlayer.pause();
        }

        // disable notifications
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        //when you click on rematch it crashes because this intent is empty
        if (getIntent().getExtras().getBoolean("MULTIPLAYER")) {
            IS_MULTIPLAYER = true;

            if (getIntent().getExtras().getBoolean("SERVER")) {
                IS_SERVER = true;
                onSeedReady((Seed) getIntent().getSerializableExtra("SEED_OBJECT"));
            }

            if (getIntent().getExtras().getBoolean("CLIENT")) {
                IS_CLIENT = true;
                new Seed(getIntent().getExtras().getString("SEED_STRING"), this);
            }
        } else {
            new Seed(this);
        }

        opponentName = getIntent().getExtras().getString("OPPONENT_NAME");

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

    }

    private void initFrames() {
        buttonMultiplier = (Button) findViewById(R.id.game_btn_multiplier);
        buttonMultiplier.setClickable(false);
//        buttonMultiplier.setVisibility(View.INVISIBLE);
//        buttonMultiplier.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.user.consumeMultiplier();
//                surfaceView.setMultiplierActive(true);
//                if (MainActivity.soundEffectsPlaying) {
//                    playSound(Sound.WOW);
//                }
//                MainActivity.animateButton(getApplicationContext(), buttonMultiplier, R.anim.pop_out);
//
//                animate(pregameButtonFrame, false, 1);
//                activeFrame = null;
//            }
//        });

        txtvwWaiting = (TextView) findViewById(R.id.game_txtvw_waiting);
        txtvwWaiting.setVisibility(View.INVISIBLE);

        buttonReady = (Button) findViewById(R.id.game_btn_ready);
        buttonReady.setClickable(true);
        buttonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.playerReady = true;

                if(IS_MULTIPLAYER) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (IS_SERVER) {
                                    Server.out.writeUTF("other_player_ready");
                                }
                                if (IS_CLIENT) {
                                    Client.out.writeUTF("other_player_ready");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    if (surfaceView.otherPlayerReady) {
                        surfaceView.bothPlayersReady = true;
                        hidePregameFrame();
                    } else {
                        buttonReady.setClickable(false);
                        buttonReady.setAlpha(0.25f);
                        txtvwWaiting.setVisibility(View.VISIBLE);
                    }

                } else {
                    surfaceView.bothPlayersReady = true;
                    hidePregameFrame();
                }
            }
        });

        buttonMutesfx = (ImageButton) findViewById(R.id.game_btn_mutesfx);
        if (MainActivity.soundEffectsPlaying)
            buttonMutesfx.setImageResource(R.drawable.button_play);
        else if (!MainActivity.soundEffectsPlaying)
            buttonMutesfx.setImageResource(R.drawable.button_mute);

        buttonMutesfx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.soundEffectsPlaying = !MainActivity.soundEffectsPlaying;

                if (MainActivity.soundEffectsPlaying) {
                    buttonMutesfx.setImageResource(R.drawable.button_play);
                }
                if (!MainActivity.soundEffectsPlaying) {
                    buttonMutesfx.setImageResource(R.drawable.button_mute);
                }

                MainActivity.animateButton(getApplicationContext(), buttonMutesfx, R.anim.button_clicked);
            }
        });

        buttonMute = (ImageButton) findViewById(R.id.game_btn_mute);
        if (MainActivity.musicPlaying)
            buttonMute.setImageResource(R.drawable.musicplaying);
        if (!MainActivity.musicPlaying)
            buttonMute.setImageResource(R.drawable.musicmute);

        buttonMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.musicPlaying = !MainActivity.musicPlaying;

                if (MainActivity.musicPlaying) {
                    mediaPlayer.start();
                    buttonMute.setImageResource(R.drawable.musicplaying);
                } else if (!MainActivity.musicPlaying) {
                    mediaPlayer.pause();
                    buttonMute.setImageResource(R.drawable.musicmute);
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
        txtvwOtherNameHint = (TextView) findViewById(R.id.game_txtvw_opponent_hint);
        txtvwOtherName = (TextView) findViewById(R.id.game_txtvw_opponent_name);
        txtvwOtherNameHint.setTypeface(MainActivity.tf);
        txtvwOtherName.setTypeface(MainActivity.tf);

        if (IS_MULTIPLAYER) {
            txtvwOtherName.setText(opponentName + "");
        } else {
            txtvwOtherName.setText("Niemand :'^(");
        }
//        MainActivity.animateButton(getApplicationContext(), buttonMultiplier, R.anim.pop_in);
//        if (MainActivity.user.multipliers > 0) {
//            buttonMultiplier.setClickable(true);
//            buttonMultiplier.setAlpha(1);
//        } else {
//            buttonMultiplier.setClickable(false);
//            buttonMultiplier.setAlpha(0.25f);
//        }

        pauseButtonFrame = (FrameLayout) findViewById(R.id.game_fl_pause);
        pauseButtonFrame.setVisibility(View.INVISIBLE);
        buttonMute.setClickable(false);
        buttonMutesfx.setClickable(false);
        buttonLeave.setClickable(false);

        aftermathButtonFrame = (FrameLayout) findViewById(R.id.game_fl_aftermath);
        aftermathButtonFrame.setVisibility(View.INVISIBLE);
        buttonRematch.setClickable(false);
        buttonQuit.setClickable(false);

        activeFrame = ActiveFrame.PREGAME;
    }

    public void hidePregameFrame() {
        if (activeFrame != null)
            activeFrame = null;

        animate(pregameButtonFrame, false, 0);

        buttonReady.setClickable(false);
//            MainActivity.animateButton(getApplicationContext(), buttonMute, R.anim.pop_out);
////            animate(pregameButtonFrame, false, 1);
//            buttonMultiplier.setClickable(false);
//
//            MainActivity.animateButton(getApplicationContext(), buttonMutesfx, R.anim.pop_out);
////            animate(pregameButtonFrame, false, 1);
//            buttonMultiplier.setClickable(false);
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
        i.putExtra("DISTANCE", surfaceView.distanceCount / 10);
        startActivity(i);

        surfaceView.aftermathWindow = null;
    }

    private void animate(final FrameLayout fl, final boolean load, final int dir) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Animation aOut;
                Animation aIn;

                if (dir == 0) {
                    aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_out);
                    aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
                } else {
                    aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_out);
                    aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
                }

                aOut.reset();
                aOut.setFillAfter(true);

                aIn.reset();
                aIn.setFillAfter(true);

                if (load) {
                    fl.clearAnimation();
                    fl.startAnimation(aIn);
                } else {
                    fl.clearAnimation();
                    fl.startAnimation(aOut);
                }

                fl.bringToFront();
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
        if (surfaceView == null)
            return;

        if(!surfaceView.activeStates.contains(GameSurfaceView.GameState.START_GAME)) {
            if (!surfaceView.activeStates.contains(GameSurfaceView.GameState.END_GAME) && (activeFrame == null || activeFrame == ActiveFrame.PREGAME)) {
                if (!IS_MULTIPLAYER) {
                    surfaceView.pauseGame(true);

                    pauseButtonFrame.setVisibility(View.VISIBLE);
                    animate(pauseButtonFrame, true, 0);

                    buttonMute.setClickable(true);
                    buttonLeave.setClickable(true);
                    buttonMutesfx.setClickable(true);

                    //if (MainActivity.musicPlaying)
                    //    mediaPlayer.pause();

                    activeFrame = ActiveFrame.PAUSE;
                }
            } else {
                switch (activeFrame) {
                    case PAUSE:
                        if (!IS_MULTIPLAYER) {
                            surfaceView.pauseGame(false);

                            pauseButtonFrame.setVisibility(View.INVISIBLE);
                            animate(pauseButtonFrame, false, 0);

                            buttonMute.setClickable(false);
                            buttonLeave.setClickable(false);
                            buttonMutesfx.setClickable(false);

                            //if (MainActivity.musicPlaying)
                            //    mediaPlayer.start();

                            activeFrame = null;
                        }
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

    public void playSound(final Sound sound) {

        if (MainActivity.soundEffectsPlaying) {
            new Thread(new Runnable() {
                @Override
                public void run() {

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
            }).start();
        }
    }


        class Read implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (surfaceView != null) {
                            if (surfaceView.opponent != null) {

                                String result = "";
                                if (IS_SERVER)
                                    result = Server.in.readUTF();
                                else if (IS_CLIENT) {
                                    result = Client.in.readUTF();
                                }

                                if (!result.isEmpty()) {
                                    if (result.contains("destroy_")) {
                                        LinkedList<GameObject> toDestroy = new LinkedList<>(surfaceView.gameObjects);
                                        for (GameObject g : toDestroy) {
                                            if (g.objectID == (Integer.parseInt(result.split("_")[2])))
                                                g.destroyExternally();
                                        }
                                    }
                                } else {
                                    switch (result) {
                                        case "sync_update_counter":
                                            if (IS_CLIENT) {
                                                surfaceView.updateCounter = 0;
                                            }
                                            break;
                                        case "end_game":
                                            surfaceView.activateState(GameSurfaceView.GameState.END_GAME);
                                            activeFrame = ActiveFrame.AFTERMATH;
                                            break;
                                        case "pause":
                                            onBackPressed();
                                            break;
                                        case "resume":
                                            onBackPressed();
                                            break;
                                        case "ducat":
                                            surfaceView.player.ducatCounter++;
                                            break;
                                        case "other_player_ready":
                                            surfaceView.otherPlayerReady = true;

                                            if(surfaceView.playerReady) {
                                                surfaceView.bothPlayersReady = true;
                                                hidePregameFrame();
                                            }
                                            break;
                                        default:
                                            surfaceView.opponent.targetX = surfaceView.laneXValues.get(Integer.parseInt(result.split("-")[0]));
                                            surfaceView.opponent.targetY = surfaceView.laneYValues.get(Integer.parseInt(result.split("-")[1]));
                                            break;
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

        class WriteReady implements Runnable {
            @Override
            public void run() {
                try {
                    if (IS_SERVER)
                        Server.out.writeBoolean(true);
                    if (IS_CLIENT)
                        Client.out.writeBoolean(true);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//    class ReadDeath implements Runnable {
//        @Override
//        public void run() {
//            //while (true) {
//                try {
//                    if (IS_SERVER) {
//                        if (Server.in.readChar() == 's')
//                            surfaceView.activateState(GameSurfaceView.GameState.END_GAME);
//                        if (Server.in.readChar() == 'c')
//                            surfaceView.activateState(GameSurfaceView.GameState.END_GAME);
//                    }
//                    if (IS_CLIENT) {
//                        if (Client.in.readChar() == 's')
//                            surfaceView.activateState(GameSurfaceView.GameState.END_GAME);
//                        if (Client.in.readChar() == 'c')
//                            surfaceView.activateState(GameSurfaceView.GameState.END_GAME);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            //}
//        }
//    }

        class ReadReady implements Runnable {
            @Override
            public void run() {

                try {
                    if (IS_SERVER)
                        Server.in.readBoolean();
                    if (IS_CLIENT)
                        Client.in.readBoolean();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                opponentReady = true;
                if (IS_MULTIPLAYER) {
                    new Thread(new Read()).start();
                    //new Thread(new ReadDeath()).start();
                }
            }
        }

        @Override
        public void onSeedReady (Seed seed){

            // game surface view init
            surfaceView = (GameSurfaceView) findViewById(R.id.game_srfcvw);
            surfaceView.setActivity(this);

            // give metrics to surface view
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            surfaceView.setMetrics(metrics);

            surfaceView.seed = seed;

            distanceCounter = (TextView) findViewById(R.id.game_txtvw_counter);
            distanceCounter.setTypeface(MainActivity.tf);
            surfaceView.setDistanceCounter(distanceCounter);

            initFrames();

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
            if (IS_MULTIPLAYER) {
                new Thread(new ReadReady()).start();
                new Thread(new WriteReady()).start();
            }
        }
    }
