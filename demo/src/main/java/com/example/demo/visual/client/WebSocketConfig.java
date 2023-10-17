package com.example.demo.visual.client;

import org.java_websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketClient webSocketClient(){
        try {
            String url = "ws://localhost:8001/ws/message";
            WebSocketClient client = new EmptyClient(new URI(url ));
            client.connect();
            return client;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
