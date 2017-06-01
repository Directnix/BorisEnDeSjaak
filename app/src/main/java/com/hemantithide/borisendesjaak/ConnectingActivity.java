package com.hemantithide.borisendesjaak;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.R;

import java.io.IOException;

public class ConnectingActivity extends AppCompatActivity {

    Client client;
    boolean connected = false;

    Button pingBtn;
    TextView connTv;

    Activity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);

        String ip = getIntent().getExtras().getString("IP");

        connTv = (TextView) findViewById(R.id.con_tv_connecting);
        connTv.setText("Connecting to " + ip);

        pingBtn = (Button) findViewById(R.id.con_btn_ping);
        pingBtn.setVisibility(View.INVISIBLE);

        try {
            client = new Client(ip);

            new Thread(new HandleConnection()).start();
        } catch (IOException e) {
            connTv.setText("Can't connect to server.");
        }
    }

    void updateUI() {
        self.runOnUiThread(new Runnable() {
            public void run() {
                connTv.setText("Connected succesfully");
                pingBtn.setVisibility(View.VISIBLE);
                pingBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Write()).start();
                    }
                });
            }
        });
    }

    class Write implements Runnable{
        @Override
        public void run() {
            try {
                client.out.writeBoolean(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class HandleConnection implements Runnable {
        @Override
        public void run() {
            while (!connected) {
                if (client.connected) {
                    connected = true;
                }
            }
            updateUI();
        }
    }
}
