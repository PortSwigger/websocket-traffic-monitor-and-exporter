package data;

import burp.api.montoya.websocket.WebSocket;

public class WebSocketWrapper {
    private final int number;
    private final String url;
    private final WebSocket webSocket;

    public WebSocketWrapper(int number, String url, WebSocket webSocket) {
        this.number = number;
        this.url = url;
        this.webSocket = webSocket;
    }

    public int getNumber() {
        return number;
    }

    public String getUrl() {
        return url;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public String toString() {
        return number + ". " + url;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WebSocketWrapper that = (WebSocketWrapper) obj;
        return webSocket.equals(that.webSocket);
    }

    @Override
    public int hashCode() {
        return webSocket.hashCode();
    }
}