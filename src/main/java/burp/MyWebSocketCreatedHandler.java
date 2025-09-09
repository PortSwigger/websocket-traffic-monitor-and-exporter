package burp;

import burp.api.montoya.websocket.WebSocketCreated;
import burp.api.montoya.websocket.WebSocketCreatedHandler;
import data.MessageData;
import data.UIUpdater;
import data.WebSocketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyWebSocketCreatedHandler implements WebSocketCreatedHandler {
    private final Map<Object, List<MessageData>> allWebSocketsMap;
    private final Map<Object, List<MessageData>> visibleWebSocketsMap;
    private final UIUpdater uiUpdater;
    private final MyWebSocketMessageTab tab;
    private final AtomicInteger webSocketCounter = new AtomicInteger(1);

    public MyWebSocketCreatedHandler(Map<Object, List<MessageData>> allWebSocketsMap, Map<Object, List<MessageData>> visibleWebSocketsMap, UIUpdater uiUpdater, MyWebSocketMessageTab tab) {
        this.allWebSocketsMap = allWebSocketsMap;
        this.visibleWebSocketsMap = visibleWebSocketsMap;
        this.uiUpdater = uiUpdater;
        this.tab = tab;
    }

    @Override
    public void handleWebSocketCreated(WebSocketCreated webSocketCreated) {
        String url = webSocketCreated.upgradeRequest().url();

        WebSocketWrapper wrapper = new WebSocketWrapper(
                webSocketCounter.getAndIncrement(),
                url,
                webSocketCreated.webSocket(),
                webSocketCreated.toolSource().toolType().toolName()
        );

        allWebSocketsMap.put(wrapper, new ArrayList<>());

        MessageCapture messageHandler = new MessageCapture(allWebSocketsMap, visibleWebSocketsMap, wrapper, uiUpdater, tab);
        webSocketCreated.webSocket().registerMessageHandler(messageHandler);
    }
}
