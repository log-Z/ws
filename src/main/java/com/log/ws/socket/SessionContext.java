package com.log.ws.socket;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket会话上下文
 */
public class SessionContext {

    /** WebSocket会话 */
    private final WebSocketSession session;

    /** 用户ID */
    private final String userId;

    /** 变量 */
    private final Map<String, Object> variables = new HashMap<>();

    public SessionContext(WebSocketSession session, String userId) {
        this.session = session;
        this.userId = userId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * 获取变量
     * @param name  变量名
     * @param cls   变量类型
     * @return      返回变量的值，当未找到变量时返回null，当变量类型不匹配时返回null。
     */
    @SuppressWarnings("unchecked")
    public <T> T getVariables(String name, Class<T> cls) {
        Object o = variables.get(name);
        if (cls.isInstance(o)) {
            return (T) o;
        } else {
            return null;
        }
    }

    /**
     * 新增或修改变量
     * @param name  变量名
     * @param value 变量值
     */
    public void putVariables(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * 删除变量
     * @param name  变量名
     */
    public void removeVariables(String name) {
        variables.remove(name);
    }

}
