package com.hemantithide.borisendesjaak;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Daniel on 03/06/2017.
 */

public class AccountFrame extends FrameLayout {

    private MainActivity main;

    private TextView hint_gamesPlayed;
    private TextView hint_gamesWon;
    private TextView hint_ducatsCollected;
    private TextView hint_applesCollected;
    private TextView hint_kinkersCollected;
    private TextView hint_longestDistance;
    private TextView hint_mostDucats;

    private TextView stat_gamesPlayed;
    private TextView stat_gamesWon;
    private TextView stat_ducatsCollected;
    private TextView stat_applesCollected;
    private TextView stat_kinkersCollected;
    private TextView stat_longestDistance;
    private TextView stat_mostDucats;

    private LinkedList<TextView> hintViews;
    private LinkedList<TextView> statViews;

    public AccountFrame(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountFrame(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AccountFrame(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void init() {

        hintViews = new LinkedList<>();
        statViews = new LinkedList<>();

        hintViews.add(hint_gamesPlayed = (TextView)main.findViewById(R.id.account_hint_games_played));
        hintViews.add(hint_gamesWon = (TextView)main.findViewById(R.id.account_hint_games_won));
        hintViews.add(hint_ducatsCollected = (TextView)main.findViewById(R.id.account_hint_ducats));
        hintViews.add(hint_applesCollected = (TextView)main.findViewById(R.id.account_hint_apples));
        hintViews.add(hint_kinkersCollected = (TextView)main.findViewById(R.id.account_hint_kinkers));
        hintViews.add(hint_longestDistance = (TextView)main.findViewById(R.id.account_hint_distance));
        hintViews.add(hint_mostDucats = (TextView)main.findViewById(R.id.account_hint_most_ducats));

        statViews.add(stat_gamesPlayed = (TextView)main.findViewById(R.id.account_games_played));
        statViews.add(stat_gamesWon = (TextView)main.findViewById(R.id.account_games_won));
        statViews.add(stat_ducatsCollected = (TextView)main.findViewById(R.id.account_ducats));
        statViews.add(stat_applesCollected = (TextView)main.findViewById(R.id.account_apples));
        statViews.add(stat_kinkersCollected = (TextView)main.findViewById(R.id.account_kinkers));
        statViews.add(stat_longestDistance = (TextView)main.findViewById(R.id.account_longest_distance));
        statViews.add(stat_mostDucats = (TextView)main.findViewById(R.id.account_most_ducats));

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");

        Log.e("views", hintViews + " / " + statViews);

        for(TextView hv : hintViews) {
            if (hv != null)
                hv.setTypeface(tf);
        }

        for(TextView sv : statViews)
            if (sv != null)
                sv.setTypeface(tf);

        update();
    }

    public void update() {
        hint_gamesPlayed.setText(getResources().getString(R.string.account_games_played) + "   ");
        hint_gamesWon.setText(getResources().getString(R.string.account_games_won) + "   ");
        hint_ducatsCollected.setText(getResources().getString(R.string.account_ducats_collected) + "   ");
        hint_applesCollected.setText(getResources().getString(R.string.account_apples_collected) + "   ");
        hint_kinkersCollected.setText(getResources().getString(R.string.account_kinkers_collected) + "   ");
        hint_longestDistance.setText(getResources().getString(R.string.account_longest_distance) + "   ");
        hint_mostDucats.setText(getResources().getString(R.string.account_most_ducats) + "   ");

        stat_gamesPlayed.setText("   " + MainActivity.user.gamesPlayed + "");
        stat_gamesWon.setText("   " + MainActivity.user.gamesWon + "");
        stat_ducatsCollected.setText("   " + MainActivity.user.ducatsCollected + "");
        stat_applesCollected.setText("   " + MainActivity.user.applesCollected + "");
        stat_kinkersCollected.setText("   " + MainActivity.user.kinkersCollected + "");
        stat_longestDistance.setText("   " + MainActivity.user.longestDistance + "m");
        stat_mostDucats.setText("   " + MainActivity.user.mostDucats + "");
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }
}
