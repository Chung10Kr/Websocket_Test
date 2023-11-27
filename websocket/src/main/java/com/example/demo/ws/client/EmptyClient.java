package com.example.demo.ws.client;

import com.example.demo.ws.server.WebSocketHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class EmptyClient extends WebSocketClient {

    private final WebSocketHandler webSocketHandler;

    public EmptyClient(URI serverUri, Draft draft, WebSocketHandler webSocketHandler) {
        super(serverUri, draft);
        this.webSocketHandler = webSocketHandler;
    }

    public EmptyClient(URI serverURI,WebSocketHandler webSocketHandler) {
        super(serverURI);
        this.webSocketHandler = webSocketHandler;
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

    @Override
    public void onMessage(String message) {
        System.out.println("다른 서버로 부터 받은 메세지" + message);
        webSocketHandler.sendMsg(message);
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