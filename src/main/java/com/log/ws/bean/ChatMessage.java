package com.log.ws.bean;

import java.util.UUID;

/**
 * 聊天信息
 */
public class ChatMessage {

    /** ID */
    private final String id = UUID.randomUUID().toString();

    /** 用户ID */
    private String userId;

    /** 讨论组ID */
    private String groupId;

    /** 聊天内容 */
    private String content;

    /** 时间戳 */
    private final long timestamp = System.currentTimeMillis();

    public ChatMessage() {
    }

    public ChatMessage(String userId, String groupId, String content) {
        this.userId = userId;
        this.groupId = groupId;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
