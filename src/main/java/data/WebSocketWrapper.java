package data;

import burp.api.montoya.websocket.WebSocket;

public class WebSocketWrapper {
    private final int number;
    private final String url;
    private final WebSocket webSocket;
    private final String toolSource;

    public WebSocketWrapper(int number, String url, WebSocket webSocket,  String toolSource) {
        this.number = number;
        this.url = url;
        this.webSocket = webSocket;
        this.toolSource = toolSource;
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

    public String getToolSource(){
        return toolSource;
    }

    public String getCsvFilename() {
        String sanitizedUrl = url.replaceAll("[^a-zA-Z0-9.-]", "_");
        return number + "-" + sanitizedUrl + "-" + toolSource;
    }

    @Override
    public String toString() {
        return number + ". " + url + " - " + toolSource;
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