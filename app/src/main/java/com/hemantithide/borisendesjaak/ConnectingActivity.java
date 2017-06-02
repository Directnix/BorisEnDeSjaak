package com.hemantithide.borisendesjaak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.R;

public class ConnectingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_connecting);

        TextView connTv = (TextView) findViewById(R.id.con_tv_connecting);
        connTv.setText(getIntent().getExtras().getString("IP"));
    }
}
