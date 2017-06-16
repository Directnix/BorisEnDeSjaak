package com.hemantithide.borisendesjaak.Engine;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.MainActivity;
import com.hemantithide.borisendesjaak.R;

import java.util.HashSet;

/**
 * Created by Daniel on 16/06/2017.
 */

public class GameNotificationManager {

    private static GameActivity activity;

    public enum Notification { APPLE, BORIS, FIREBALL, FIREWAVE, COMPLIMENT }
    public static HashSet<Notification> checklist = new HashSet<>();

    public static void showNotification(Notification type, boolean limitToOnce) {

        if(!limitToOnce || !checklist.contains(type)) {

            activity.playSound(GameActivity.Sound.WHISTLE);

            switch(type) {
                case APPLE:
                    GameSurfaceView.notification = activity.getResources().getString(R.string.game_popup_apples);
                    GameSurfaceView.notificationTimer = 300;
                    break;
                case BORIS:
                    GameSurfaceView.notification = activity.getResources().getString(R.string.game_popup_boris);
                    GameSurfaceView.notificationTimer = 300;
                    break;
                case FIREBALL:
                    GameSurfaceView.notification = activity.getResources().getString(R.string.game_popup_fireball);
                    GameSurfaceView.notificationTimer = 300;
                    break;
                case FIREWAVE:
                    GameSurfaceView.notification = activity.getResources().getString(R.string.game_popup_firewave);
                    GameSurfaceView.notificationTimer = 300;
                    break;
            }

            GameSurfaceView.notificationAlpha = 255;

            if(limitToOnce)
                checklist.add(type);
        }
    }

    public static void setActivity(GameActivity activity) {
        GameNotificationManager.activity = activity;
    }
}
