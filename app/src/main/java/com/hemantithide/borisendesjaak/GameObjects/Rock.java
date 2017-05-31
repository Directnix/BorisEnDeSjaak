package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 31/05/2017.
 */

public class Rock extends GameObject {

    private int lifespan;

    public Rock(GameSurfaceView game, int ID) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.rock);

        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 10, true);

        laneID = game.primaryRocks.get(ID);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

//        canvas.drawCircle(100, 100, 50, paint);

        canvas.drawBitmap(sprite, posX + 16, posY, null);
    }

    public void update() {
        posX = game.getLanePositionValues().get(laneID);

        lifespan += game.metrics.heightPixels / 90;

        posY = lifespan;
    }
}
