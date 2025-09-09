package burp;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.websocket.*;
import data.MessageData;
import data.UIUpdater;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static burp.api.montoya.core.ByteArray.byteArray;

public class MessageCapture implements MessageHandler {
    private final Map<Object, List<MessageData>> allWebSocketsMap;
    private final Map<Object, List<MessageData>> visibleWebSocketsMap;
    private final Object webSocketKey;
    private final UIUpdater uiUpdater;
    private final MyWebSocketMessageTab tab;

    public MessageCapture(Map<Object, List<MessageData>> allWebSocketsMap, Map<Object, List<MessageData>> visibleWebSocketsMap, Object webSocketKey, UIUpdater uiUpdater, MyWebSocketMessageTab tab) {
        this.allWebSocketsMap = allWebSocketsMap;
        this.visibleWebSocketsMap = visibleWebSocketsMap;
        this.webSocketKey = webSocketKey;
        this.uiUpdater = uiUpdater;
        this.tab = tab;
    }

    @Override
    public TextMessageAction handleTextMessage(TextMessage message) {
        if (tab.isCaptureEnabled()) {
            processMessage(message.direction().toString(), byteArray(message.payload()), false);
        }

        return TextMessageAction.continueWith(message);
    }

    @Override
    public BinaryMessageAction handleBinaryMessage(BinaryMessage message) {
        if (tab.isCaptureEnabled()) {
            processMessage(message.direction().toString(), message.payload(), true);
        }

        return BinaryMessageAction.continueWith(message);
    }

    private void processMessage(String direction, ByteArray payload, boolean isBinary) {
        MessageData messageData = new MessageData(
                direction,
                payload,
                isBinary,
                payload.length(),
                new Date()
        );

        allWebSocketsMap.get(webSocketKey).add(messageData);

        if (!visibleWebSocketsMap.containsKey(webSocketKey)) {
            visibleWebSocketsMap.put(webSocketKey, new ArrayList<>());
            uiUpdater.notifyMapChanged();
        }

        visibleWebSocketsMap.get(webSocketKey).add(messageData);
        uiUpdater.notifyDataChanged();
    }
}
