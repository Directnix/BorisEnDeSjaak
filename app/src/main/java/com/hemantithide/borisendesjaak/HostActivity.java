package com.hemantithide.borisendesjaak;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hemantithide.borisendesjaak.Network.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HostActivity extends AppCompatActivity {

    Server server = null;
    boolean connect = false;

    TextView clientTv;
    ImageView qrIv;
    Button startBtn;
    
    Activity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        qrIv = (ImageView) findViewById(R.id.host_iv_qr);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        qrIv.setImageBitmap(generateQRBitMap(String.valueOf(Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()))));

        clientTv = (TextView) findViewById(R.id.host_tv_ip);
        
        startBtn = (Button)findViewById(R.id.host_btn_start);
        startBtn.setVisibility(View.INVISIBLE);

        try {
            server = new Server();
            new Thread(new Read()).start();
        } catch (IOException e) {
            clientTv.setText("Iets ging mis :'^(");
        }
    }

    void updateUI() {
        self.runOnUiThread(new Runnable() {
            public void run() {
                clientTv.setText("Iemand is verbonden");
                qrIv.setVisibility(View.GONE);
                
                startBtn.setVisibility(View.VISIBLE);
                startBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 01-Jun-17 START GAME 
                    }
                });
            }
        });
    }


    class Read implements Runnable {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (server.connected) {

                        if (!connect) {
                            updateUI();
                            connect = true;
                        }

                        try {
                            // TODO: 01-Jun-17  Handle startup write
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 100);
        }
    }


    private Bitmap generateQRBitMap(final String content) {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
}
