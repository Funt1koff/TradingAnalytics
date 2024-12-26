package com.funtikov.trading.client;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.exceptions.WebSocketClientException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Mono;

@ClientWebSocket("${integration.twelvedata.ws-url}")
@Slf4j
public abstract class TwelveDataWSClient implements AutoCloseable {

    private final Sinks.Many<String> incomingMessages =
            Sinks.many().multicast().onBackpressureBuffer();

    // Сингл-эмиттер, который "отпустим" при открытии сессии
    private final Sinks.One<Void> sessionReady = Sinks.one();

    private WebSocketSession session;

    @OnOpen
    public void onOpen(WebSocketSession session) {
        this.session = session;
        log.info("TwelveData WebSocket connection opened");
        // Сигнализируем, что сессия открыта
        sessionReady.tryEmitValue(null);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("Received message from TwelveData WS: {}", message);
        incomingMessages.tryEmitNext(message).orThrow();
    }

    @OnClose
    public void onClose(WebSocketSession session) {
        log.info("TwelveData WebSocket connection closed");
        this.session = null;
        // Завершаем поток входящих сообщений
        incomingMessages.tryEmitComplete();
    }

    /**
     * Возвращает Publisher входящих сообщений.
     */
    public Flux<String> getIncomingMessages() {
        return incomingMessages.asFlux();
    }

    /**
     * Возвращает Mono, которое завершится,
     * когда сессия WebSocket действительно откроется.
     */
    public Mono<Void> waitForSessionOpen() {
        return sessionReady.asMono();
    }

    /**
     * Отправить сообщение в WebSocket-сессию (асинхронно).
     */
    public Mono<Void> sendAsync(String message) {
        // Сначала дожидаемся открытия сессии
        return waitForSessionOpen()
                .then(Mono.fromRunnable(() -> {
                    if (session == null || !session.isOpen()) {
                        throw new WebSocketClientException("No available and open WebSocket session");
                    }
                    session.sendSync(message);
                }));
    }

    /**
     * Для совместимости — если нужен синхронный метод send().
     */
    public void send(String message) {
        sendAsync(message).block();
    }
}
