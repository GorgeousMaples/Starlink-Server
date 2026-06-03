package com.app.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer {

//    /**
//     * 消息代理配置（发给谁）
//     */
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // 客户端订阅地址前缀
//        registry.enableSimpleBroker("/topic", "/queue");
//
//        // 客户端发送消息的前缀
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//    /**
//     * WebSocket 端点（客户端连哪）
//     */
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*") // 生产环境要限制
//                .withSockJS(); // 支持 SockJS 降级
//    }

    @Bean
    public UnityWebSocketHandler unityWebSocketHandler() {
        return new UnityWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(unityWebSocketHandler(), "/ws/unity")
                .setAllowedOriginPatterns("*");
    }
}