package com.app.config;

import com.app.domain.bo.BroadcastMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UnityWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        System.out.println("客户端连接； " + sessionId);
        SESSIONS.put(sessionId, session);
        sendMessage("GetSessionId", sessionId, sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        SESSIONS.remove(session.getId());
    }

    /**
     * 构建 json 信息
     */
    private <T> TextMessage buildMessage(String url, T data) {
        try {
            BroadcastMessage<T> msg = new BroadcastMessage<>(url, data);
            String json = OBJECT_MAPPER.writeValueAsString(msg);
            return new TextMessage(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("构建广播消息失败！", e);
        }
    }

    /**
     * 发送单体消息
     */
    private void sendMessage(TextMessage message, String sessionId) {
        try {
            if (SESSIONS.containsKey(sessionId)) {
                WebSocketSession session = SESSIONS.get(sessionId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("消息发送失败！", e);
        }
    }

    /**
     * 发送单体消息
     */
    private <T> void sendMessage(String url, T data, String sessionId) {
        TextMessage message = buildMessage(url, data);
        sendMessage(message, sessionId);
    }

    /**
     * 给所有订阅者发送广播
     */
    public <T> void broadcast(String url, T data) {
        TextMessage message = buildMessage(url, data);
        for (String sessionId : SESSIONS.keySet()) {
            sendMessage(message, sessionId);
        }
    }

    /**
     * 排除发送者的广播
     */
    public <T> void broadcast(String url, T data, String senderId) {
        TextMessage message = buildMessage(url, data);
        for (String sessionId : SESSIONS.keySet()) {
            // 排除发送者
            if (sessionId.equals(senderId)) continue;
            sendMessage(message, sessionId);
        }
    }

    /**
     * 给指定用户发送广播
     */
    public <T> void broadcast(String url, T data, List<String> sessions) {
        TextMessage message = buildMessage(url, data);
        for (String sessionId : sessions) {
            sendMessage(message, sessionId);
        }
    }

    /**
     * 给指定用户发送广播（排除发送者）
     */
    public <T> void broadcast(String url, T data, List<String> sessions, String senderId) {
        TextMessage message = buildMessage(url, data);
        for (String sessionId : sessions) {
            if (sessionId.equals(senderId)) continue;
            sendMessage(message, sessionId);
        }
    }
}

