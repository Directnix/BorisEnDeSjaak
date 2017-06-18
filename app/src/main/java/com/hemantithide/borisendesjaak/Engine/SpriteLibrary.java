package com.hemantithide.borisendesjaak.Engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hemantithide.borisendesjaak.MainActivity;
import com.hemantithide.borisendesjaak.R;
import com.hemantithide.borisendesjaak.User;

import java.util.HashMap;

import static com.hemantithide.borisendesjaak.Engine.SpriteLibrary.Sprite.*;

/**
 * Created by Daniel on 02/06/2017.
 */

public class SpriteLibrary {

    private GameSurfaceView game;
    static SpriteLibrary instance;

    public enum Sprite {
        PLAYER,
        OPPONENT,
        DRAGON,
        SHOOTFIRE,
        ROCK,
        FIREBALL,
        APPLE, APPLE_ICON,
        KINKER,
        DUCAT, DUCAT_ICON,
        WARNING,
        BACKGROUND, TUTORIAL
    }

    public static HashMap<Sprite, Bitmap> bitmaps = new HashMap<>();

    public SpriteLibrary(GameSurfaceView game, int attractionID) {

        instance = this;

        Bitmap sprite;

        User.Attraction attraction = User.Attraction.values()[attractionID];

        switch(attraction) {
            case BORIS:
                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.grassloop_plus);
                bitmaps.put(BACKGROUND, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.schapen_compact);
                sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 1.5), (int)(sprite.getHeight() / 1.5), true);
                bitmaps.put(PLAYER, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.draken);
                sprite = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4), 0, sprite.getWidth() / 4, sprite.getHeight());
                bitmaps.put(DRAGON, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.vuurspuw_compact);
                bitmaps.put(SHOOTFIRE, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.rock);
                sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 10, true);
                bitmaps.put(ROCK, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.vuurballen_compact);
                sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 1.5), (int)(sprite.getHeight() / 1.5), true);
                bitmaps.put(FIREBALL, sprite);

                break;
            case VOGELROK:
                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.cloud);
                bitmaps.put(BACKGROUND, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.player_bird);
                sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 0.3), (int)(sprite.getHeight() / 0.3), true);
                bitmaps.put(PLAYER, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.bird);
                sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 0.75), (int)(sprite.getHeight() / 0.75), true);
                bitmaps.put(DRAGON, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.vuurspuw_compact);
                bitmaps.put(SHOOTFIRE, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.rock);
                sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 10, true);
                bitmaps.put(ROCK, sprite);

                sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.vuurballen_compact);
                sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 1.5), (int)(sprite.getHeight() / 1.5), true);
                bitmaps.put(FIREBALL, sprite);

                break;
        }

        Log.e("Attraction set to", attraction.toString() + "");

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.apple);
        bitmaps.put(APPLE, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.apple);
        Bitmap newSprite = Bitmap.createScaledBitmap(sprite, sprite.getWidth() / 2, sprite.getHeight() / 2, true);
        bitmaps.put(APPLE_ICON, newSprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.kinker);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 8, game.metrics.widthPixels / 10, true);
        bitmaps.put(KINKER, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.ducat);
        sprite = Bitmap.createScaledBitmap(sprite, sprite.getWidth() / 3, sprite.getHeight() / 3, true);
        bitmaps.put(DUCAT, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.ducat);
        sprite = Bitmap.createScaledBitmap(sprite, sprite.getWidth() / 6, sprite.getHeight() / 6, true);
        bitmaps.put(DUCAT_ICON, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.warning_icon);
        sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 1.5), (int)(sprite.getHeight() / 1.5), true);
        bitmaps.put(WARNING, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.tutorial);
        sprite = Bitmap.createScaledBitmap(sprite, (sprite.getWidth()), (sprite.getHeight()), true);
        bitmaps.put(TUTORIAL, sprite);
    }
}
