package burp;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.websocket.*;
import data.MessageData;
import data.UIUpdater;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static burp.api.montoya.core.ByteArray.byteArray;

public class MessageCapture implements MessageHandler {
    private final Logging logger;
    private final Map<Object, List<MessageData>> allWebSocketsMap;
    private final Map<Object, List<MessageData>> visibleWebSocketsMap;
    private final Object webSocketKey;
    private final UIUpdater uiUpdater;
    private final MyWebSocketMessageTab tab;

    public MessageCapture(Logging logger, Map<Object, List<MessageData>> allWebSocketsMap, Map<Object, List<MessageData>> visibleWebSocketsMap, Object webSocketKey, UIUpdater uiUpdater, MyWebSocketMessageTab tab) {
        this.logger = logger;
        this.allWebSocketsMap = allWebSocketsMap;
        this.visibleWebSocketsMap = visibleWebSocketsMap;
        this.webSocketKey = webSocketKey;
        this.uiUpdater = uiUpdater;
        this.tab = tab;
    }

    public TextMessageAction handleTextMessage(TextMessage message) {
        if (tab.isCaptureEnabled()) {
            MessageData messageData = new MessageData(
                    message.direction().toString(),
                    byteArray(message.payload()),
                    false,
                    message.payload().length(),
                    new Date()

            );
            
            allWebSocketsMap.get(webSocketKey).add(messageData);
            
            if (!visibleWebSocketsMap.containsKey(webSocketKey)) {
                visibleWebSocketsMap.put(webSocketKey, new ArrayList<>());
                uiUpdater.notifyMapChanged();
            }
            
            visibleWebSocketsMap.get(webSocketKey).add(messageData);
            uiUpdater.notifyDataChanged();
            
            logger.logToOutput("Captured text message: " + message.payload());
        }
        return TextMessageAction.continueWith(message);
    }

    public BinaryMessageAction handleBinaryMessage(BinaryMessage message) {
        if (tab.isCaptureEnabled()) {
            MessageData messageData = new MessageData(
                    message.direction().toString(),
                    message.payload(),
                    true,
                    message.payload().length(),
                    new Date()
            );
            
            allWebSocketsMap.get(webSocketKey).add(messageData);
            
            if (!visibleWebSocketsMap.containsKey(webSocketKey)) {
                visibleWebSocketsMap.put(webSocketKey, new ArrayList<>());
                uiUpdater.notifyMapChanged();
            }
            
            visibleWebSocketsMap.get(webSocketKey).add(messageData);
            uiUpdater.notifyDataChanged();
            
            logger.logToOutput("Captured binary message: " + message.payload().length() + " bytes");
        }
        return BinaryMessageAction.continueWith(message);
    }





}
