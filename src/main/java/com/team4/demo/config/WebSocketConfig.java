package com.team4.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 建立頻道，前端頁面要訂閱的頻道的前綴字開頭
        config.enableSimpleBroker("/topic", "/queue", "/user", "/member");
        
        // 表示 queue 这个頻道用於一對一發送訊息的，默認是 'user'
//        config.setUserDestinationPrefix("/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // 這個是 Spring Boot 2.4.0 之後的寫法
                .withSockJS();
    }


}
