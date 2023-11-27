package com.example.demo.ws.client;

import com.example.demo.ws.server.WebSocketHandler;
import org.java_websocket.client.WebSocketClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@RestController
public class wsController {

    private final WebSocketHandler webSocketHandler;
    public wsController(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
    private WebSocketClient webSocketClient;

    @GetMapping("/ws/server")
    public String connectSvr(){
        this.webSocketClient = createWebSocketClient();
        webSocketHandler.setWebSocketClient(this.webSocketClient);
        return "연결 완료";
    }
    public WebSocketClient createWebSocketClient(){
        try {
            String url = "ws://localhost:7002/ws/message";
            WebSocketClient client = new EmptyClient(new URI(url),webSocketHandler);
            client.connect();
            return client;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/ws/send/server")
    public String sendMsgToSvr(){
        CompletableFuture.runAsync(() -> {
            webSocketClient.send("HI! FROM WS111");
        });
        return "성공";
    }

}
