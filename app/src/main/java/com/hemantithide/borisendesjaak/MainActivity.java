package com.hemantithide.borisendesjaak;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import layout.BlankFragment;
import layout.SecondFragment;

public class MainActivity extends AppCompatActivity {

    Fragment f1 = new BlankFragment();
    Fragment f2 = new SecondFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.main_framelayout) != null && savedInstanceState == null) {
            BlankFragment blankFragment = new BlankFragment();
            blankFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout, blankFragment, "blank").commit();
        }

        Button btnChangeFragment = (Button) findViewById(R.id.ma_btn_switch);
        btnChangeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment = new SecondFragment();
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.ma_fragment, fragment);
//                ft.commit();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Fragment secondFragment = fragmentManager.findFragmentByTag("second");

                if (secondFragment == null) {
                    transaction.replace(R.id.main_framelayout, new SecondFragment(), "second");
                    transaction.addToBackStack(null);
                }
                else {
                    transaction.replace(R.id.main_framelayout, secondFragment, "second");
                    transaction.addToBackStack(null);
                }

                transaction.commit();
            }
        });
    }
}
