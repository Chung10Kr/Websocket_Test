package com.example.demo.ws.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class EmptyClient extends WebSocketClient {


    public EmptyClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public EmptyClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        reconnect();
    }

    long beforeTime =  System.nanoTime();
    @Override
    public void onMessage(String message) {

        if( message.equals("1")){
            beforeTime = System.nanoTime();
        }

        System.out.println("다른 서버로 부터 받은 메세지" + message);

        if( message.equals("1000000")){
            long afterTime = System.nanoTime();
            long diffTime = afterTime - beforeTime; // 두 시간의 차이 계산 (나노초)
            double seconds = (double) diffTime / 1_000_000_000.0; // 나노초를 초로 변환
            System.out.println("시간 차이(s): " + seconds);
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

}
