import burp.api.montoya.logging.Logging;
import burp.api.montoya.websocket.WebSocketCreated;
import burp.api.montoya.websocket.WebSocketCreatedHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyWebSocketCreatedHandler implements burp.api.montoya.websocket.WebSocketCreatedHandler {
    private final Logging logging;
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private final UIUpdater uiUpdater;

    public MyWebSocketCreatedHandler(Logging logging, Map<Object, List<MessageData>> messagesByWebSocket, UIUpdater uiUpdater) {
        this.logging = logging;
        this.messagesByWebSocket = messagesByWebSocket;
        this.uiUpdater = uiUpdater;
    }

    @Override
    public void handleWebSocketCreated(burp.api.montoya.websocket.WebSocketCreated webSocketCreated) {
        messagesByWebSocket.put(webSocketCreated.webSocket(), new ArrayList<>());
        
        MessageCapture messageHandler = new MessageCapture(logging, messagesByWebSocket, webSocketCreated.webSocket(), uiUpdater);
        webSocketCreated.webSocket().registerMessageHandler(messageHandler);
        
        uiUpdater.notifyMapChanged();
    }
}
