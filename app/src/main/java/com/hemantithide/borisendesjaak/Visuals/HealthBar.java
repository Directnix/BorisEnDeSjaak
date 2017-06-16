package com.hemantithide.borisendesjaak.Visuals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.GameObjects.Sheep;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 31/05/2017.
 */

public class HealthBar {

    private GameObject sheep;
    private Bitmap sprite;

    public HealthBar(GameObject sheep) {
        this.sheep = sheep;
        sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_3);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, sheep.posX - (sprite.getWidth()/2), sheep.posY - (sheep.sprite.getHeight() / 3), paint);
    }

    public void update(int newHealth) {
        switch(newHealth) {
            case 0:
                sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_0);
                break;
            case 1:
                sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_1);
                break;
            case 2:
                sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_2);
                break;
            case 3:
                sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_3);
                break;
        }
    }
}
