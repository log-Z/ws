package com.log.ws.socket;

/**
 * WebSocket数据报
 */
public class WebSocketDatagram {
    /** 状态码 */
    private int status;

    /** 错误信息 */
    private String error;

    /** 数据 */
    private Object data;

    public WebSocketDatagram() {
    }

    public WebSocketDatagram(Object data) {
        this.data = data;
    }

    public WebSocketDatagram(int status, String error) {
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
