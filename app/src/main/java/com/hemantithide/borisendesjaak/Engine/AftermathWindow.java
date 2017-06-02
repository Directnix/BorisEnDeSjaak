package com.hemantithide.borisendesjaak.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;

/**
 * Created by Daniel on 02/06/2017.
 */

class AftermathWindow {

    private GameSurfaceView game;

    private int lifetime;

    public AftermathWindow(GameSurfaceView game) {
        this.game = game;
    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        Typeface tf = Typeface.createFromAsset(game.getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");
        paint.setTypeface(tf);

        paint.setTextAlign(Paint.Align.CENTER);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2)) ;

        paint.setTextSize(0.056f * lifetime);
        canvas.drawText("De winnaar is...", xPos, yPos - (canvas.getHeight() / 16), paint);

        paint.setTextSize(0.072f * lifetime);
        canvas.drawText(game.player.getUsername() + "!", xPos, yPos, paint);
//        canvas.drawText("De winnaar is: \n" + game.player.getUsername() + "!", game.metrics.widthPixels / 2, game.metrics.heightPixels / 2, paint);
    }

    public void update() {
        if(lifetime < 1000)
            lifetime += 10;
    }
}
