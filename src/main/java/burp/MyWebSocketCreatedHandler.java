package burp;

import burp.api.montoya.logging.Logging;
import data.MessageData;
import data.UIUpdater;
import data.WebSocketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyWebSocketCreatedHandler implements burp.api.montoya.websocket.WebSocketCreatedHandler {
    private final Logging logging;
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private final UIUpdater uiUpdater;
    private final AtomicInteger webSocketCounter = new AtomicInteger(1);

    public MyWebSocketCreatedHandler(Logging logging, Map<Object, List<MessageData>> messagesByWebSocket, UIUpdater uiUpdater) {
        this.logging = logging;
        this.messagesByWebSocket = messagesByWebSocket;
        this.uiUpdater = uiUpdater;
    }

    @Override
    public void handleWebSocketCreated(burp.api.montoya.websocket.WebSocketCreated webSocketCreated) {
        String url = webSocketCreated.upgradeRequest().url();

        WebSocketWrapper wrapper = new WebSocketWrapper(
            webSocketCounter.getAndIncrement(),
            url,
            webSocketCreated.webSocket()
        );
        
        messagesByWebSocket.put(wrapper, new ArrayList<>());
        
        MessageCapture messageHandler = new MessageCapture(logging, messagesByWebSocket, wrapper, uiUpdater);
        webSocketCreated.webSocket().registerMessageHandler(messageHandler);
        
        uiUpdater.notifyMapChanged();
    }
}
