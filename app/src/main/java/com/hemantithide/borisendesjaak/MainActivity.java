package com.hemantithide.borisendesjaak;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hemantithide.borisendesjaak.Engine.UsernameGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Button playBtn;
    private ZXingScannerView scannerView;
    private MediaPlayer mediaPlayer;

    MainActivity self = this;

    FrameLayout mainFrame, settings_frame, shop_frame, play_frame, qr_frame, qr_make_frame, random_name_frame, custom_name_frame;
    FrameLayout currentFrame;

    public static boolean musicPlaying = true;

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

    private ShopInfoLayout shopInfoLayout;
    private Button shop_info_btn_purchase;

    private AccountFrame accountFrame;

    Typeface tf;

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
                animate(qr_frame, qr_make_frame, 0);
                ImageView qrIv = (ImageView) findViewById(R.id.main_iv_code);

                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                qrIv.setImageBitmap(generateQRBitMap(String.valueOf(Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()))));
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
        settings_btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            musicPlaying = !musicPlaying;

            if (musicPlaying) {
                mediaPlayer.start();
                settings_btn_music.setImageResource(R.drawable.button_play);
            }
            if (!musicPlaying) {
                mediaPlayer.pause();
                settings_btn_music.setImageResource(R.drawable.button_mute);
            }
            }
        });

//        Button languageBtn = (Button) findViewById(R.id.main_btn_language);
//        languageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

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
        } else if (currentFrame.equals(qr_make_frame)) {
            animate(qr_make_frame, qr_frame, 1);
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

    void initFrames() {
        mainFrame = (FrameLayout) findViewById(R.id.main_fl_mainMenu);
        currentFrame = mainFrame;

        play_frame = (FrameLayout) findViewById(R.id.main_fl_play);
        play_frame.setVisibility(View.INVISIBLE);

        qr_frame = (FrameLayout) findViewById(R.id.main_fl_friend);
        qr_frame.setVisibility(View.INVISIBLE);

        qr_make_frame = (FrameLayout) findViewById(R.id.main_fl_make);
        qr_make_frame.setVisibility(View.INVISIBLE);

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
    }

    private void animate(FrameLayout from, FrameLayout to, int dir) {
        Animation aOut;
        Animation aIn;

        if (dir == 0) {
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

        from.clearAnimation();
        from.startAnimation(aOut);

        to.clearAnimation();
        to.startAnimation(aIn);

        currentFrame = to;
        currentFrame.bringToFront();
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

    private Bitmap generateQRBitMap(final String content) {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();

        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    bmp.setPixel(x , y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
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

                    animate(shopInfoLayout, shop_frame, 1);
                }
                break;
            case CUSTOM_NAME:
                if(user.ducats >= 1001) {
                    initCustomNameFrame();
                }
                break;
            case FREE_PICTURE:
                if(user.ducats >= 500) {
                    animate(shopInfoLayout, shop_frame, 1);
                }
        }

        updateShopFrame();
    }
}
