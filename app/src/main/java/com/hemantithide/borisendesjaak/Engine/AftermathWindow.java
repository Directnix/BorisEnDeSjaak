package com.hemantithide.borisendesjaak.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.GameActivity;

/**
 * Created by Daniel on 02/06/2017.
 */

class AftermathWindow {

    private GameSurfaceView game;

    private int lifetime;

    private boolean showRewards;
    private boolean doneShowing;

    private int nextActionTimer;
    private int actionCounter;

    private int distanceDucats;
    private int collectedDucats;
    private int victoryDucats;
    private int totalDucats;

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
        int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2));

        paint.setTextSize(0.056f * lifetime);
        canvas.drawText("De winnaar is...", xPos, yPos - (canvas.getHeight() / 16), paint);

        paint.setTextSize(0.072f * lifetime);
        canvas.drawText(game.player.getUsername() + "!", xPos, yPos, paint);


        if (showRewards) {
            paint.setTextSize(48);

            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Afstand:   ", xPos, (int) (yPos + (canvas.getHeight() * 0.2)), paint);
            canvas.drawText("Dukaten:   ", xPos, (int) (yPos + (canvas.getHeight() * 0.25)), paint);
            canvas.drawText("Gewonnen:   ", xPos, (int) (yPos + (canvas.getHeight() * 0.3)), paint);
            canvas.drawText("Totaal:   ", xPos, (int) (yPos + (canvas.getHeight() * 0.4)), paint);

            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("  +" + distanceDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.2)), paint);
            canvas.drawText("  +" + collectedDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.25)), paint);
            canvas.drawText("  +" + victoryDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.3)), paint);
            canvas.drawText("  +" + totalDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.4)), paint);
        }
    }

    public void update() {
        if(lifetime < 1000)
            lifetime += 10;

        if(nextActionTimer > 0)
            nextActionTimer--;

        actionCounter++;

        if(showRewards && nextActionTimer == 0) {
            if(distanceDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                distanceDucats--;
                totalDucats++;

                if(distanceDucats == 0)
                    nextActionTimer = 10;

            } else if(collectedDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                collectedDucats--;
                totalDucats++;

                if(collectedDucats == 0)
                    nextActionTimer = 10;

            } else if(victoryDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                victoryDucats--;
                totalDucats++;

                if(victoryDucats == 0)
                    nextActionTimer = 10;

            } else if(!doneShowing) {
                doneShowing = true;
                nextActionTimer = 140;
            }
        }

        if(doneShowing && nextActionTimer == 0) {
            game.endGame(totalDucats);
        }
    }

    void showRewards() {
        distanceDucats = game.frameCount / 100;
        collectedDucats = game.player.ducatCounter;
        victoryDucats = (int)((distanceDucats + collectedDucats) * 0.2);

        showRewards = true;

        nextActionTimer = 50;
    }
}
