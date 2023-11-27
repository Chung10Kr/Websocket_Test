package com.example.demo.ws.server;

import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("!!!COnnect");
        CLIENTS.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CLIENTS.remove(session.getId());
    }


    public void sendMsg(String msg){
        TextMessage textMessage = new TextMessage(msg);
        CLIENTS.entrySet().forEach( arg->{
            sendMessageToClient(arg.getValue(),textMessage);
        });
    }
    private WebSocketClient client;
    public void setWebSocketClient(WebSocketClient client){
        this.client = client;
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();  //메시지를 보낸 아이디
//        if(client!=null){
//            CompletableFuture.runAsync(() -> {
//                client.send(message.getPayload());
//            });
//        }

        CLIENTS.entrySet().forEach( arg->{
            if(!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                sendMessageToClient(arg.getValue(),message);
            }
        });
    }
    public void sendMessageToClient(WebSocketSession session,TextMessage message){
        try {
            session.sendMessage(message);
//            synchronized (session) {
//                session.sendMessage(message);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}