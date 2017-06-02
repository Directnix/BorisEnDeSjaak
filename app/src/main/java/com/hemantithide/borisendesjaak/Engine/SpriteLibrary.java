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
        ROCK,
        FIREBALL,
        APPLE,
        APPLE_SMALL,
        KINKER
    }

    public static HashMap<Sprite, Bitmap> bitmaps = new HashMap<>();

    public SpriteLibrary(GameSurfaceView game) {

        Bitmap sprite;

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.sheep_placeholder);
        sprite = Bitmap.createScaledBitmap(sprite, (game.metrics.widthPixels / 300) * 27, (game.metrics.widthPixels / 300) * 48, false);
        bitmaps.put(PLAYER, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.sheep_placeholder);
        sprite = Bitmap.createScaledBitmap(sprite, (game.metrics.widthPixels / 300) * 27, (game.metrics.widthPixels / 300) * 48, false);
        bitmaps.put(OPPONENT, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.draak);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 2, game.metrics.widthPixels / 2, true);
        bitmaps.put(DRAGON, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.rock);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 10, true);
        bitmaps.put(ROCK, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.fireball);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 5, true);
        bitmaps.put(FIREBALL, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.apple);
        bitmaps.put(APPLE, sprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.apple);
        Bitmap newSprite = Bitmap.createScaledBitmap(sprite, sprite.getWidth() / 2, sprite.getHeight() / 2, true);
        bitmaps.put(APPLE_SMALL, newSprite);

        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.kinker);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 8, game.metrics.widthPixels / 10, true);
        bitmaps.put(KINKER, sprite);
    }
}
