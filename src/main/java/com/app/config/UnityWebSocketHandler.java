package com.app.config;

import com.app.domain.Room;
import com.app.domain.bo.BroadcastMessage;
import com.app.service.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UnityWebSocketHandler extends TextWebSocketHandler {
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>(); // key 为 sessionId
    private static final Map<String, WebSocketSession> PLAYER_SESSIONS = new ConcurrentHashMap<>(); // key 为 playerId
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private RoomService roomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String sessionId = session.getId();
        System.out.println("客户端连接； " + sessionId);
        SESSIONS.put(sessionId, session);
        TextMessage message = buildMessage("GetSessionId", sessionId);
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        SESSIONS.remove(session.getId());
        System.out.println("会话关闭 ：" + session.getId());
        String playerId = (String) session.getAttributes().get("playerId");
        if (playerId != null) {
            PLAYER_SESSIONS.remove(playerId);
            roomService.leaveRoom(playerId);
        }
    }

    /**
     * 绑定玩家与会话
     */
    public void bindSession(String playerId, String sessionId) {
        WebSocketSession session = SESSIONS.get(sessionId);
        session.getAttributes().put("playerId", playerId); // 将玩家 ID 存入会话中
        PLAYER_SESSIONS.put(playerId, session);
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
    private void sendMessage(TextMessage message, String playerId) {
        try {
            if (PLAYER_SESSIONS.containsKey(playerId)) {
                WebSocketSession session = PLAYER_SESSIONS.get(playerId);
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
    private <T> void sendMessage(String url, T data, String playerId) {
        TextMessage message = buildMessage(url, data);
        sendMessage(message, playerId);
    }

    /**
     * 给所有订阅者发送广播
     */
    public <T> void broadcast(String url, T data) {
        TextMessage message = buildMessage(url, data);
        for (String playerId : PLAYER_SESSIONS.keySet()) {
            sendMessage(message, playerId);
        }
    }

    /**
     * 排除发送者的广播
     */
    public <T> void broadcast(String url, T data, String senderId) {
        TextMessage message = buildMessage(url, data);
        for (String playerId : PLAYER_SESSIONS.keySet()) {
            // 排除发送者
            if (playerId.equals(senderId)) continue;
            sendMessage(message, playerId);
        }
    }

    /**
     * 给指定用户发送广播
     */
    public <T> void broadcast(String url, T data, List<String> playerIds) {
        TextMessage message = buildMessage(url, data);
        for (String playerId : playerIds) {
            sendMessage(message, playerId);
        }
    }

    /**
     * 给指定用户发送广播（排除发送者）
     */
    public <T> void broadcast(String url, T data, List<String> playerIds, String senderId) {
        TextMessage message = buildMessage(url, data);
        for (String playerId : playerIds) {
            if (playerId.equals(senderId)) continue;
            sendMessage(message, playerId);
        }
    }

    /**
     * 给房间的所有人发送广播
     */
    public <T> void broadcast(String url, T data, Room room, String senderId) {
        broadcast(url, data, room.getAllPlayers(), senderId);
    }

}

