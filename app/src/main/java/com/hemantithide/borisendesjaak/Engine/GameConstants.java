package com.hemantithide.borisendesjaak.Engine;

/**
 * Created by Daniel on 08/06/2017.
 */

public class GameConstants {


    //General
    public static int GAME_SPEED = 5;
    public static int ANIMATION_SPEED = 10;
    public static int FPS = 1000/60;


    //Game movement
    public static int SWIPE_SPEED_HORIZONTAL = 20;
    public static int SWIPE_SPEED_VERTICAL = 15;
    public static int GAME_SPEED_INCREASE_INTERVAL = 140;
    public static int WAVE_SPAWN_INTERVAL = 140;

    //Dragon
    public static int DRAGON_FIREBALL_INTERVAL = 64;
    public static int DRAGON_PRESENT_TIMER = 700;
    public static int DRAGON_ABSENT_TIMER = 1400;
    public static int DRAGON_FIREWAVE_CHARGE_TIMER = 125;

    //Surface view updates
    public static int INIT_UPDATE_COUNTER_VALUE = -180;
    public static int INIT_DRAGON_ABSENT_TIMER = 825;

    //Sheep
    public static int SHEEP_HEALTH = 3;
    public static int SHEEP_REQUIRED_APPLES = 10;
    public static int SHEEP_REQUIRED_APPLES_INCREASE = 5;
    public static int SHEEP_COLLISION_TIMER = 180;
    public static int SHEEP_KINKER_TIMER = 445;

    //Username generator
    public static int APPROPRIATE_AGE = 16;

    //Network
    public static int PORT = 8000;


}
