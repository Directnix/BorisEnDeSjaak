package com.hemantithide.borisendesjaak;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.hemantithide.borisendesjaak.Engine.UsernameGenerator;
import java.util.ArrayList;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Button playBtn;
    private ZXingScannerView scannerView;
    MediaPlayer mediaPlayer;

    MainActivity self = this;

    ArrayList<FrameLayout> frames = new ArrayList<>();
    FrameLayout mainFrame, settings_frame, shop_frame, play_frame, qr_frame, random_name_frame, custom_name_frame;
    FrameLayout currentFrame;

    public static boolean musicPlaying = true;
    public static boolean soundEffectsPlaying = true;

    private UsernameGenerator usernameGenerator;
    public static User user;

    private TextView shop_txtvw_ducats;
    private Button play_btn_friend;
    private Button play_btn_random;
    private Button qr_btn_make;
    private Button qr_btn_scan;
    private ImageButton main_btn_settings;
    private ImageButton main_btn_shop;
    private ImageButton main_btn_account;
    private ImageButton settings_btn_music;
    private ImageButton settings_btn_mutesfx;

    private Button username_option_A;
    private Button username_option_B;
    private Button username_option_C;
    private TextView randomUsernameTxtvw;

    private Spinner custom_name_firstAdjSpinnner;
    private Spinner custom_name_secondAdjSpinner;
    private Spinner custom_name_nounSpinner;

    private Button shop_btn_random_name;
    private Button shop_btn_custom_name;
    private Button shop_btn_multiplier;
    private Button shop_btn_free_picture;
    private ImageView ducats;
    private static int cheatClicks = 0;
    private Button invisbleCheatButton;

    private ShopInfoLayout shopInfoLayout;
    private Button shop_info_btn_purchase;

    private AccountFrame accountFrame;

    static Typeface tf;

    //private NFC nfc;
    //private Tag tag;
    private NfcAdapter nfcAdapter;
    public static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // generate user object
        if(User.load(this) != null) {
            user = User.load(this);
            usernameGenerator = new UsernameGenerator(user.age, user.gender);
        } else {
            user = new User(22, User.Gender.MALE);
            usernameGenerator = new UsernameGenerator(user.age, user.gender);
            user.setUsername(usernameGenerator.generateUsername());
        }

        // init text views
        final TextView welcomeTxtvw = (TextView)findViewById(R.id.main_txtvw_welcome);
        randomUsernameTxtvw = (TextView)findViewById(R.id.main_txtvw_username);
        shop_txtvw_ducats = (TextView)findViewById(R.id.main_txtvw_ducats);

        randomUsernameTxtvw.setText(user.username);
        shop_txtvw_ducats.setText(user.ducats + "");


        // nfc init
       // nfc = new NFC(this);

        //if user doesn't have nfc
//        PackageManager pm = getPackageManager();
//        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC))
//        {
//            // This device does not have a compass, turn off the compass feature
//            nfcAdapter.disableReaderMode(this);
//            //disableCompassFeature();
//        }



        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");
        randomUsernameTxtvw.setTypeface(tf,Typeface.BOLD);
        welcomeTxtvw.setTypeface(tf);
        shop_txtvw_ducats.setTypeface(tf);



        //Start music loop
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sjaaksong);
        mediaPlayer.setLooping(true);
        if (musicPlaying)
            mediaPlayer.start();
        else
            mediaPlayer.pause();

        initFrames();

        // if game had ended, update user account
        if(getIntent().getSerializableExtra("DUCATS") != null) {
            user.gamesPlayed++;
            if((boolean)getIntent().getSerializableExtra("WON")) user.gamesWon++;
            user.applesCollected += ((int) getIntent().getSerializableExtra("C_APPLES"));
            user.ducatsCollected += ((int) getIntent().getSerializableExtra("C_DUCATS"));
            user.kinkersCollected += ((int) getIntent().getSerializableExtra("C_KINKERS"));
            user.distanceTravelled += ((int) getIntent().getSerializableExtra("DISTANCE"));
            user.ducatsEarned += ((int) getIntent().getSerializableExtra("DUCATS"));

            if((int) getIntent().getSerializableExtra("DISTANCE") > user.longestDistance) user.longestDistance = ((int) getIntent().getSerializableExtra("DISTANCE"));
            if((int) getIntent().getSerializableExtra("DUCATS") > user.mostDucats) user.mostDucats = ((int) getIntent().getSerializableExtra("DUCATS"));

            user.addToDucats((int) getIntent().getSerializableExtra("DUCATS"));

            user.save(getApplicationContext());
            accountFrame.update();
        }

        //creating variables
        final ImageView backgroundOne = (ImageView) findViewById(R.id.main_imgvw_backgroundOne);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.main_imgvw_backgroundTwo);
        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        //setting animator up
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(100000L);
        //actual method
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        this.playBtn = (Button) findViewById(R.id.main_btn_play);
        playBtn.setTypeface(tf);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(mainFrame, play_frame, 0);
            }
        });

        play_btn_friend = (Button) findViewById(R.id.main_btn_friend);
        play_btn_friend.setTypeface(tf);
        play_btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(play_frame, qr_frame, 0);
            }
        });

        play_btn_random = (Button) findViewById(R.id.main_btn_random);
        play_btn_random.setTypeface(tf);
        play_btn_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                i.putExtra("USERNAME", randomUsernameTxtvw.getText());
                startActivity(i);
            }
        });

        qr_btn_make = (Button) findViewById(R.id.main_btn_make);
        qr_btn_make.setTypeface(tf);
        qr_btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HostActivity.class);
                startActivity(i);
            }
        });

        qr_btn_scan = (Button) findViewById(R.id.main_btn_scan);
        qr_btn_scan.setTypeface(tf);
        qr_btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            scannerView = new ZXingScannerView(getApplicationContext());

            if (!haveCameraPermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                }
                recreate();
            }

            setContentView(scannerView);
            scannerView.setResultHandler(self);
            scannerView.startCamera();
            }
        });

        main_btn_settings = (ImageButton) findViewById(R.id.main_btn_settings);
        main_btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(mainFrame, settings_frame, 0);
            }
        });

        settings_btn_music = (ImageButton) findViewById(R.id.main_btn_mute);
        if(musicPlaying)
            settings_btn_music.setImageResource(R.drawable.musicplaying);
        else if(!musicPlaying)
            settings_btn_music.setImageResource(R.drawable.musicmute);

        settings_btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            musicPlaying = !musicPlaying;

            if (musicPlaying) {
                mediaPlayer.start();
                settings_btn_music.setImageResource(R.drawable.musicplaying);

            }
            if (!musicPlaying) {
                mediaPlayer.pause();
                settings_btn_music.setImageResource(R.drawable.musicmute);

            }

            animateButton(getApplicationContext(), settings_btn_music, R.anim.button_clicked);
            }
        });

        settings_btn_mutesfx = (ImageButton) findViewById(R.id.main_btn_mutesfx);
        if(soundEffectsPlaying)
            settings_btn_mutesfx.setImageResource(R.drawable.button_play);
        else if(!soundEffectsPlaying)
            settings_btn_mutesfx.setImageResource(R.drawable.button_mute);


        settings_btn_mutesfx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffectsPlaying = !soundEffectsPlaying;
                if(soundEffectsPlaying)
                    settings_btn_mutesfx.setImageResource(R.drawable.button_play);
                if(!soundEffectsPlaying)
                    settings_btn_mutesfx.setImageResource(R.drawable.button_mute);

                animateButton(getApplicationContext(), settings_btn_mutesfx, R.anim.button_clicked);
            }
        });

        setLanguageButtons();

//        Button languageBtn = (Button) findViewById(R.id.main_btn_language);
//        languageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        //create cheat menu shit here.
        ducats = (ImageView)findViewById(R.id.shopinfo_imgvw_ducats2);
        ducats.setClickable(true);
        ducats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cheatClicks += 1;
                Log.e("mainActivity", "you clicked the ducat");
                if (cheatClicks > 10)
                {
                    Intent i = new Intent(getApplicationContext(), cheatMenu.class);
                    startActivity(i);
                }
            }
        });

        invisbleCheatButton = (Button)findViewById(R.id.shopinfo_btn_cheatsbtn);
        invisbleCheatButton.setClickable(true);
        invisbleCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cheatClicks += 1;
                Log.e("mainActivity", "you clicked the ducat");
                if (cheatClicks > 10)
                {
                    Intent i = new Intent(getApplicationContext(), cheatMenu.class);
                    startActivity(i);
                }
            }
        });

        shop_btn_random_name = (Button) findViewById(R.id.main_btn_new_username);
        shop_btn_random_name.setTypeface(tf);
        shop_btn_random_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(shop_frame, shopInfoLayout, 0);
                shopInfoLayout.init(ShopInfoLayout.Item.RANDOM_NAME);
            }
        });

        shop_btn_custom_name = (Button) findViewById(R.id.main_btn_custom_username);
        shop_btn_custom_name.setTypeface(tf);
        shop_btn_custom_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(shop_frame, shopInfoLayout, 0);
                shopInfoLayout.init(ShopInfoLayout.Item.CUSTOM_NAME);
            }
        });

        shop_btn_multiplier = (Button) findViewById(R.id.shop_btn_multiplier);
        shop_btn_multiplier.setTypeface(tf);
        shop_btn_multiplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(shop_frame, shopInfoLayout, 0);
                shopInfoLayout.init(ShopInfoLayout.Item.MULTIPLIER);
            }
        });

        shop_btn_free_picture = (Button) findViewById(R.id.shop_btn_free_picture);
        shop_btn_free_picture.setTypeface(tf);
        shop_btn_free_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(shop_frame, shopInfoLayout, 0);
                shopInfoLayout.init(ShopInfoLayout.Item.FREE_PICTURE);
            }
        });

        main_btn_shop = (ImageButton) findViewById(R.id.main_btn_shop);
        main_btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(mainFrame, shop_frame, 0);
            }
        });

        main_btn_account = (ImageButton) findViewById(R.id.main_btn_account);
        main_btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(mainFrame, accountFrame, 0);
            }
        });

        final TextView username_comment = (TextView) findViewById(R.id.username_comment);
        username_comment.setTypeface(tf);

        username_option_A = (Button) findViewById(R.id.username_option_A);
        username_option_A.setTypeface(tf);
        username_option_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username_option_A.getText() + "");
                user.save(getApplicationContext());

                randomUsernameTxtvw.setText(user.username);
                animate(random_name_frame, shop_frame, 0);
            }
        });

        username_option_B = (Button) findViewById(R.id.username_option_B);
        username_option_B.setTypeface(tf);
        username_option_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username_option_B.getText() + "");
                user.save(getApplicationContext());

                randomUsernameTxtvw.setText(user.username);
                animate(random_name_frame, shop_frame, 0);
            }
        });

        username_option_C = (Button) findViewById(R.id.username_option_C);
        username_option_C.setTypeface(tf);
        username_option_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username_option_C.getText() + "");
                user.save(getApplicationContext());

                randomUsernameTxtvw.setText(user.username);
                animate(random_name_frame, shop_frame, 0);
            }
        });

        final TextView username_accept = (Button) findViewById(R.id.username_accept_custom);
        username_accept.setTypeface(tf);
        username_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(usernameGenerator.generateUsername(
                        (UsernameGenerator.Adjective)custom_name_firstAdjSpinnner.getSelectedItem(),
                        (UsernameGenerator.Adjective)custom_name_secondAdjSpinner.getSelectedItem(),
                        (UsernameGenerator.Noun)custom_name_nounSpinner.getSelectedItem()
                ));
                user.subtractFromDucats(1001);
                user.save(getApplicationContext());

                randomUsernameTxtvw.setText(user.username);
                updateShopFrame();

                animate(custom_name_frame, shop_frame, 1);
            }
        });

        initCustomNameSpinners();

        // update shop frame
        updateShopFrame();
    }

    private void setLanguageButtons() {
        ImageButton dutchBtn= (ImageButton) findViewById(R.id.button_dutch);
        dutchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration = new Configuration(getResources().getConfiguration());
                configuration.locale = new Locale("nl");
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                recreate();
            }
        });

        ImageButton frenchBtn = (ImageButton) findViewById(R.id.button_french);
        frenchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration = new Configuration(getResources().getConfiguration());
                configuration.locale = new Locale("fr");
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                recreate();
            }
        });

        ImageButton germanBtn= (ImageButton) findViewById(R.id.button_german);
        germanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration = new Configuration(getResources().getConfiguration());
                configuration.locale = new Locale("de");
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                recreate();
            }
        });

        ImageButton englishBtn= (ImageButton) findViewById(R.id.button_uk);
        englishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration = new Configuration(getResources().getConfiguration());
                configuration.locale = new Locale("def");
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                recreate();
            }
        });
    }

    private void initCustomNameSpinners() {

        custom_name_firstAdjSpinnner = (Spinner) findViewById(R.id.username_spnnr_first_adj);

//        LinkedList<UsernameGenerator.Adjective> spinnerAdjArray =  new LinkedList<>();
//        for(UsernameGenerator.Adjective adj : usernameGenerator.firstAdjectiveArray) {
//            spinnerAdjArray.add(adj);
//        }

        ArrayList<UsernameGenerator.Adjective> spinnerAdjArray = new ArrayList<>(usernameGenerator.firstAdjectiveArray);
        ArrayAdapter adjAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, spinnerAdjArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "RobotoCondensed-BoldItalic.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "RobotoCondensed-BoldItalic.ttf");
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(getResources().getColor(R.color.Yellow, null));

                return v;
            }


        };

        custom_name_firstAdjSpinnner.setAdapter(adjAdapter);

        ///

        custom_name_secondAdjSpinner = (Spinner) findViewById(R.id.username_spnnr_second_adj);

        spinnerAdjArray = new ArrayList<>(usernameGenerator.secondAdjectiveArray);
        adjAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, spinnerAdjArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "RobotoCondensed-BoldItalic.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "RobotoCondensed-BoldItalic.ttf");
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(getResources().getColor(R.color.Yellow, null));

                return v;
            }
        };

        custom_name_secondAdjSpinner.setAdapter(adjAdapter);

        ///

        custom_name_nounSpinner = (Spinner) findViewById(R.id.username_spnnr_noun);

        ArrayList<UsernameGenerator.Noun> spinnerNounArray =  new ArrayList<>(usernameGenerator.nounArray);
        ArrayAdapter nounAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, spinnerNounArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "RobotoCondensed-BoldItalic.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "RobotoCondensed-BoldItalic.ttf");
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(getResources().getColor(R.color.Yellow, null));

                return v;
            }
        };

        custom_name_nounSpinner.setAdapter(nounAdapter);
    }

    private void updateShopFrame() {
        shop_txtvw_ducats.setText(user.ducats + "");

//        if(user.ducats < 1001) {
//            shop_btn_custom_name.setAlpha(0.25f);
//            shop_btn_custom_name.setClickable(false);
//        } else {
//            shop_btn_custom_name.setAlpha(1);
//            shop_btn_custom_name.setClickable(true);
//        }
//
//        if(user.ducats < 100) {
//            shop_btn_random_name.setAlpha(0.25f);
//            shop_btn_random_name.setClickable(false);
//        } else {
//            shop_btn_random_name.setAlpha(1);
//            shop_btn_random_name.setClickable(true);
//        }
//
//        if(user.ducats < 50) {
//            shop_btn_multiplier.setAlpha(0.25f);
//            shop_btn_multiplier.setClickable(false);
//        } else {
//            shop_btn_multiplier.setAlpha(1);
//            shop_btn_multiplier.setClickable(true);
//        }
    }
    public static void callUpdateShop()
    {

    }


    private void initRandomNameFrame() {
        username_option_A.setText(usernameGenerator.generateUsername());
        username_option_B.setText(usernameGenerator.generateUsername());
        username_option_C.setText(usernameGenerator.generateUsername());
    }

    private void initCustomNameFrame() {

    }

    @Override
    public void onBackPressed() {
        if (currentFrame.equals(settings_frame)) {
            animate(settings_frame, mainFrame, 1);
            settings_btn_music.setClickable(false);
        } else if (currentFrame.equals(shop_frame)) {
            animate(shop_frame, mainFrame, 1);
            shop_btn_random_name.setClickable(false);
            shop_btn_custom_name.setClickable(false);
        } else if (currentFrame.equals(play_frame)) {
            animate(play_frame, mainFrame, 1);
            play_btn_friend.setClickable(false);
            play_btn_random.setClickable(false);
        } else if (currentFrame.equals(qr_frame)) {
            animate(qr_frame, play_frame,1);
            qr_btn_make.setClickable(false);
            qr_btn_scan.setClickable(false);
        } else if (currentFrame.equals(accountFrame)) {
            animate(accountFrame, mainFrame, 1);
        } else if (currentFrame.equals(random_name_frame)) {
            animate(random_name_frame, shop_frame, 1);
        } else if (currentFrame.equals(shopInfoLayout)) {
            animate(shopInfoLayout, shop_frame, 1);
        } else if (currentFrame.equals(random_name_frame)) {
            animate(random_name_frame, shopInfoLayout, 1);
        } else if (currentFrame.equals(custom_name_frame)) {
            animate(custom_name_frame, shopInfoLayout, 1);
        } else if (currentFrame.equals(mainFrame)) {
            user.save(getApplicationContext());
            System.exit(0);
        }

        currentFrame.bringToFront();
    }

    void addToFrame(FrameLayout... frame){
        for(FrameLayout f: frame){
            frames.add(f);
        }
    }

    void initFrames() {
        mainFrame = (FrameLayout) findViewById(R.id.main_fl_mainMenu);
        currentFrame = mainFrame;

        play_frame = (FrameLayout) findViewById(R.id.main_fl_play);
        play_frame.setVisibility(View.INVISIBLE);

        qr_frame = (FrameLayout) findViewById(R.id.main_fl_friend);
        qr_frame.setVisibility(View.INVISIBLE);

        settings_frame = (FrameLayout) findViewById(R.id.main_fl_settingsMenu);
        settings_frame.setVisibility(View.INVISIBLE);

        shop_frame = (FrameLayout) findViewById(R.id.main_fl_shop);
        shop_frame.setVisibility(View.INVISIBLE);

        random_name_frame = (FrameLayout) findViewById(R.id.main_fl_random_name);
        random_name_frame.setVisibility(View.INVISIBLE);

        custom_name_frame = (FrameLayout) findViewById(R.id.main_fl_custom_name);
        custom_name_frame.setVisibility(View.INVISIBLE);

        shopInfoLayout = (ShopInfoLayout) findViewById(R.id.main_fl_shop_info);
        shopInfoLayout.setVisibility(View.INVISIBLE);
        shopInfoLayout.setMain(this);
        shopInfoLayout.init(ShopInfoLayout.Item.MULTIPLIER);

        accountFrame = (AccountFrame) findViewById(R.id.main_fl_account);
        accountFrame.setVisibility(View.INVISIBLE);
        accountFrame.setMain(this);
        accountFrame.init();

        addToFrame(mainFrame, settings_frame, shop_frame, play_frame, qr_frame, random_name_frame, custom_name_frame, shopInfoLayout, accountFrame );

        disableFrames();
    }

    private void animate(FrameLayout from, FrameLayout to, int dir) {
        Animation aOut;
        Animation aIn;

        if (dir == 0) {
            aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_out);
//            aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
            aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
//            aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        } else {
            aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_out);
//            aOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_right);
            aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
//            aIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_left);
        }

        aOut.reset();
        aOut.setFillAfter(true);

        aIn.reset();
        aIn.setFillAfter(true);

        from.clearAnimation();
        from.startAnimation(aOut);

        to.clearAnimation();
        to.startAnimation(aIn);

        currentFrame = to;
        disableFrames();

        currentFrame.bringToFront();
    }

    void disableFrames(){
        for(FrameLayout f: frames){{
                ConstraintLayout layout = (ConstraintLayout) f.getChildAt(0);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    if(f.equals(currentFrame))
                        child.setClickable(true);
                    else
                        child.setClickable(false);
                }
            }
        }
    }

    static void animateButton(Context context, ImageButton button, int animID) {
        Animation anim = AnimationUtils.loadAnimation(context, animID);

//        anim.reset();
//        anim.setFillAfter(true);

        button.clearAnimation();
        button.startAnimation(anim);
    }

    static void animateButton(Context context, Button button, int animID) {
        Animation anim = AnimationUtils.loadAnimation(context, animID);

//        anim.reset();
//        anim.setFillAfter(true);

        button.clearAnimation();
        button.startAnimation(anim);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(musicPlaying)
            mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();

        if(scannerView != null)
            scannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(scannerView != null)
            scannerView.stopCamera();
        scannerView = null;
    }

    @Override
    public void handleResult(Result result) {
        Intent i = new Intent(getApplicationContext(), ConnectingActivity.class);
        i.putExtra("IP", String.valueOf(result));
        startActivity(i);
    }

    private boolean haveCameraPermission()
    {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        return this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void purchase(ShopInfoLayout.Item item) {

        switch(item) {

            case MULTIPLIER:
                if(user.ducats >= 50) {
                    user.multipliers++;

                    user.subtractFromDucats(50);
                    user.save(getApplicationContext());

                    animate(shopInfoLayout, shop_frame, 1);
                }
                break;
            case RANDOM_NAME:
                if(user.ducats >= 100) {
                    user.subtractFromDucats(100);
                    user.save(getApplicationContext());

                    initRandomNameFrame();
                    animate(shopInfoLayout, random_name_frame, 0);
                }
                break;
            case CUSTOM_NAME:
                if(user.ducats >= 1001) {
                    animate(shopInfoLayout, custom_name_frame, 0);
                }
                break;
            case FREE_PICTURE:
                if(user.ducats >= 500) {
                    animate(shopInfoLayout, shop_frame, 1);
                }
        }

        updateShopFrame();
    }
    public static void setCheatClickerZero()
    {
        cheatClicks = 0;
    }
}
