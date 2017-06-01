package com.hemantithide.borisendesjaak.Network;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Gebruiker on 6/1/2017.
 */

public class Server {

    public DataInputStream in;
    public DataOutputStream out;
    public boolean connected = false;

    ServerSocket server;

    public Server() throws IOException{
            server = new ServerSocket(8000);
            new Thread(new CatchClient()).start();
    }

    class CatchClient implements Runnable{

        @Override
        public void run() {
            try {
                Socket socket = server.accept();
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                connected = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
