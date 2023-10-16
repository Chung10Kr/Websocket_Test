package sample.webflux.websocket.netty.server;


import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerLogic {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public Mono<Void> doLogic(WebSocketSession session) {

        sessions.put(session.getId(), session);  // Add the session to the map

        return session
                .receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> {
                    sendToOtherClients(session.getId(), message);  // Send the message to other clients
                })
                .then()
                .doFinally(signalType -> sessions.remove(session.getId()));  // Remove the session from the map when it's closed
    }

    private void sendToOtherClients(String senderId, String message) {
        sessions.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(senderId))
                .forEach(entry ->
                        entry.getValue().send(Mono.just(entry.getValue().textMessage(message))).subscribe());
    }


}