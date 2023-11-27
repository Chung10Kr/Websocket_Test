package com.example.demo.socket.client;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

//@Component
public class client {

    //@Bean
    public void startclient(){
        try{
            Socket socket = new Socket("localhost", 8888);
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


            long beforeTime = System.nanoTime();
            // Send messages from 1 to 1000
            for (int i = 1; i <= 1000000; i++) {
                out.println(i);
            }
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message from Server: " + message);
                if( message.equals("1000000") ){
                    long afterTime = System.nanoTime();
                    long diffTime = afterTime - beforeTime; // 두 시간의 차이 계산 (나노초)
                    double seconds = (double) diffTime / 1_000_000_000.0; // 나노초를 초로 변환
                    System.out.println("시간 차이(s): " + seconds);
                    socket.close();
                }
            }
            System.out.println("END===");




            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
