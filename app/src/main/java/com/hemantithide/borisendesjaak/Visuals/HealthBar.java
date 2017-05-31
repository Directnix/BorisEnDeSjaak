package com.hemantithide.borisendesjaak.Visuals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameObjects.Sheep;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 31/05/2017.
 */

public class HealthBar {

    private Sheep sheep;
    private Bitmap sprite;

    public HealthBar(Sheep sheep) {
        this.sheep = sheep;
        sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_3);

//        sprite = Bitmap.createScaledBitmap(sprite, (sheep.game.metrics.widthPixels / 30) * 384, (sheep.game.metrics.widthPixels / 30) * 128, false);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, sheep.posX + 8, sheep.posY - 32, null);
    }

    public void update(int newHealth) {
        switch(newHealth) {
            case 0:
                sprite = BitmapFactory.decodeResource(sheep.game.getContext().getResources(), R.drawable.hearts_1);
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
