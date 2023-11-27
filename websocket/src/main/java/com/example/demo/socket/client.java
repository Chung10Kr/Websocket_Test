package com.example.demo.socket;


import java.io.*;
import java.net.*;

public class client {

    public void startclient(){
        try{
            Socket socket = new Socket("localhost", 8888);
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Hi");

            String message = in.readLine();
            System.out.println("Message from server: " + message);

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
