package com.example.demo.ws.client;

import org.java_websocket.client.WebSocketClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@RestController
public class wsController {
    private WebSocketClient webSocketClient;

    public WebSocketClient createWebSocketClient(){
        try {
            String url = "ws://localhost:7070/ws/message";
            WebSocketClient client = new EmptyClient(new URI(url));
            client.connect();
            return client;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/ws/server")
    public String connectSvr(){
        this.webSocketClient = createWebSocketClient();
        return "연결 완료";
    }
    @GetMapping("/ws/send/server")
    public String sendMsgToSvr(){
        CompletableFuture.runAsync(() -> {
            for(int i=1; i<=1000000; i++){
                webSocketClient.send(i+"");
            }

        });
        return "성공";
    }

}
