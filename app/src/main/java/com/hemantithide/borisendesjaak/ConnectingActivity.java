package com.hemantithide.borisendesjaak;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectingActivity extends AppCompatActivity {

    Client client;
    boolean connected = false;
    boolean connect = false;
    boolean start = false;

    TextView connTv;

    Activity self = this;
    String otherUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);

        String ip = getIntent().getExtras().getString("IP");

        connTv = (TextView) findViewById(R.id.con_tv_connecting);
        connTv.setText("Verbinden met " + ip);


        try {
            client = new Client(ip);

            new Thread(new HandleConnection()).start();
        } catch (IOException e) {
            connTv.setText("Kan niet verbinden met server");
        }
    }

    void updateUI() {
        self.runOnUiThread(new Runnable() {
            public void run() {
                connTv.setText("Verbonden met " + otherUsername);
            }
        });
    }

    class Write implements Runnable {
        @Override
        public void run() {
            try {
                client.out.writeUTF(MainActivity.username);
                new Thread(new Read()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Read implements Runnable {
        @Override
        public void run() {
            try {
                otherUsername = client.in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!connect) {
                updateUI();
                connect = true;
            }

            while(!start){
                try {
                    if(client.in.readBoolean()){
                        start = true;
                        Intent i = new Intent(getApplicationContext(), GameActivity.class);
                        startActivity(i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            new Thread(new Write()).start();
        }
    }
}
