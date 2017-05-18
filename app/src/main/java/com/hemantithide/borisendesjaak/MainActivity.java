package com.hemantithide.borisendesjaak;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    final ImageView backgroundOne = (ImageView)findViewById(R.id.main_imgvw_backgroundOne);
    final ImageView backgroundTwo = (ImageView)findViewById(R.id.main_imgvw_backgroundTwo);
    final ValueAnimator animator = ValueAnimator.ofFloat(0.0f ,1.0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());

        // Swipe

//        imgvw.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
//            @Override
//            public void onSwipeLeft() {
//                Log.e("links", " Links");
//            }
//            public void onSwipeRight() {
//                Log.e("rechts", " Rechtts");
//            }
//        });

    }
}
