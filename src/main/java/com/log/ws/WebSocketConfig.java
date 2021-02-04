package com.log.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置类
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /** 路径映射 - 聊天 */
    public static final String PATH_CHAT = "/chat/{userId}/{key}";

    /** 处理聊天信息 */
    private final WebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(WebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, PATH_CHAT)
                .setAllowedOrigins("http://coolaf.com");
    }

}
