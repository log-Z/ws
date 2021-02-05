package com.log.ws.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.ws.bean.ChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

import static com.log.ws.WebSocketConfig.PATH_CHAT;

/**
 * 处理聊天信息
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    /** 日志 */
    private final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /** WebSocket会话池 */
    private final Map<String, SessionContext> sessionPool = new HashMap<>();

    /** Jackson对象映射器 */
    private final ObjectMapper objectMapper;

    /** 路径匹配器 */
    AntPathMatcher pathMatcher = new AntPathMatcher();

    public ChatWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** 处理客户端通过WebSocket发送来的信息 */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sid = session.getId();
        SessionContext sessionContext = sessionPool.get(sid);

        // 读取聊天信息
        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        chatMessage.setUserId(sessionContext.getUserId());
        logger.info("[" + sid + "] " + chatMessage);

        // TODO: 鉴权...
        // TODO: 存储聊天信息到数据库...

        // 广播聊天信息
        broadcast(chatMessage);
    }

    /** WebSocket连接成功后 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 获取路径变量
        String path = Objects.requireNonNull(session.getUri()).getPath();
        Map<String, String> pathVariables = pathMatcher.extractUriTemplateVariables(PATH_CHAT, path);

        // TODO: 鉴权...

        // 绑定用户ID
        SessionContext sessionContext = new SessionContext(session, pathVariables.get("userId"));
        // 并注册入池
        String sid = session.getId();
        sessionPool.put(sid, sessionContext);
        logger.info("[" + sid + "] WebSocket connected. pathVariables is " + pathVariables);
    }

    /** WebSocket连接关闭后 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sid = session.getId();
        sessionPool.remove(sid);
        logger.info("[" + sid + "] WebSocket closed");
    }

    /**
     * 将聊天信息广播给讨论组中所有在线用户
     * @param chatMessage   聊天信息
     */
    private void broadcast(ChatMessage chatMessage) {
        // TODO: 从缓存查询讨论组中的所有用户...
        List<String> groupMembers = List.of("1", "2", "3", "4", "5");

        TextMessage tm = textMessage(chatMessage);
        sessionPool.entrySet().parallelStream().forEach(entry -> {
            SessionContext context = entry.getValue();
            String userId = context.getUserId();
            if (groupMembers.contains(userId)) {
                try {
                    context.getSession().sendMessage(tm);
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        });
    }

    /**
     * 从聊天信息生成WebSocket信息
     * @param chatMessage   聊天信息
     */
    private TextMessage textMessage(ChatMessage chatMessage) {
        String payload = "";
        try {
            WebSocketDatagram datagram = new WebSocketDatagram(chatMessage);
            payload = objectMapper.writeValueAsString(datagram);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }

        return new TextMessage(payload);
    }

}
