package com.hemantithide.borisendesjaak.Engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hemantithide.borisendesjaak.R;

import java.util.HashMap;

import static com.hemantithide.borisendesjaak.Engine.SpriteLibrary.Sprite.*;

/**
 * Created by Daniel on 02/06/2017.
 */

public class SpriteLibrary {

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
        WARNING
    }

    public static HashMap<Sprite, Bitmap> bitmaps = new HashMap<>();

    public SpriteLibrary(GameSurfaceView game) {

        Bitmap sprite;

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.schapen_compact);
        sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 1.5), (int)(sprite.getHeight() / 1.5), true);
        bitmaps.put(PLAYER, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.draken);
        bitmaps.put(DRAGON, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.vuurspuw_compact);
        bitmaps.put(SHOOTFIRE, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.rock);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 10, true);
        bitmaps.put(ROCK, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.vuurballen_compact);
        sprite = Bitmap.createScaledBitmap(sprite, (int)(sprite.getWidth() / 1.5), (int)(sprite.getHeight() / 1.5), true);
        bitmaps.put(FIREBALL, sprite);

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
    }
}
