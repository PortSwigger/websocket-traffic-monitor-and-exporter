package burp;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.websocket.*;
import data.MessageData;
import data.UIUpdater;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static burp.api.montoya.core.ByteArray.byteArray;

public class MessageCapture implements MessageHandler {
    private final Logging logger;
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private final WebSocket webSocket;
    private final UIUpdater uiUpdater;

    public MessageCapture(Logging logger, Map<Object, List<MessageData>> messagesByWebSocket, WebSocket webSocket, UIUpdater uiUpdater) {
        this.logger = logger;
        this.messagesByWebSocket = messagesByWebSocket;
        this.webSocket = webSocket;
        this.uiUpdater = uiUpdater;
    }

    public TextMessageAction handleTextMessage(TextMessage message) {
        MessageData messageData = new MessageData(
                message.direction().toString(),
                byteArray(message.payload()),
                false,
                message.payload().length(),
                new Date().toString()
        );
        
        messagesByWebSocket.get(webSocket).add(messageData);
        uiUpdater.notifyDataChanged();
        
        logger.logToOutput("Captured text message: " + message.payload());
        return TextMessageAction.continueWith(message);
    }

    public BinaryMessageAction handleBinaryMessage(BinaryMessage message) {
        MessageData messageData = new MessageData(
                message.direction().toString(),
                message.payload(),
                true,
                message.payload().length(),
                new Date().toString()
        );
        
        messagesByWebSocket.get(webSocket).add(messageData);
        uiUpdater.notifyDataChanged();
        
        logger.logToOutput("Captured binary message: " + message.payload().length() + " bytes");
        return BinaryMessageAction.continueWith(message);
    }





}
