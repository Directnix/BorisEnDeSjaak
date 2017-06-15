package com.hemantithide.borisendesjaak.Network;

import com.hemantithide.borisendesjaak.Engine.GameConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Gebruiker on 6/1/2017.
 */

public class Client {
    public static DataInputStream in;
    public static DataOutputStream out;
    public boolean connected = false;

    Socket socket;

    public Client(String ip) throws IOException{
        new Thread(new Connect(ip)).start();
    }

    class Connect implements Runnable{
        String ip;

        public Connect(String ip) {
            this.ip = ip;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, GameConstants.PORT);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                connected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
