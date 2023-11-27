package com.example.demo.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


//@Component
public class server {


    private static List<PrintWriter> clientOutputs = new ArrayList<>();

    //@Bean
    public void startSvr(){
        try{
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server is ready.");

            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client is connected.");

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                clientOutputs.add(out);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//                String message;
//                while ((message = in.readLine()) != null) {
//                    broadcast(message, out);
//                }
//

                Thread thread = new Thread() {
                    public void run() {
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                            String message;
                            while ((message = in.readLine()) != null) {
                                System.out.println("Message from client: " + message);
                                broadcast(message, out);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private static void broadcast(String message, PrintWriter exclude) {
        for (PrintWriter out : clientOutputs) {
            if (out != exclude) {
                out.println(message);
            }
        }
    }
}
