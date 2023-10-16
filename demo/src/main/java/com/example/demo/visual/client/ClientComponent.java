package com.example.demo.visual.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Component
public class ClientComponent implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private int serverPort = 8001;

    private String samplePath = "/ws/message";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

        Client clientOne = new Client();

        clientOne.connect(webSocketClient, getURI());

        new ClientLogic().doLogic(clientOne);

        Mono
            .delay(Duration.ofSeconds(10))
            .publishOn(Schedulers.boundedElastic())
            .subscribe(value -> {
                clientOne.disconnect();
                SpringApplication.exit(applicationContext, () -> 0);
            });
    }

    private URI getURI() {
        try {
            return new URI("ws://localhost:" + serverPort + samplePath);
        } catch (URISyntaxException USe) {
            throw new IllegalArgumentException(USe);
        }
    }
}
