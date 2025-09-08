package burp;

import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.WebSocketContextMenuEvent;
import burp.api.montoya.ui.contextmenu.WebSocketMessage;
import data.MessageData;
import data.UIUpdater;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyContextMenuItemsProvider implements ContextMenuItemsProvider {
    private Map<Object, List<MessageData>> messagesByWebSocket;
    private UIUpdater uiUpdater;

    public MyContextMenuItemsProvider(Map<Object, List<MessageData>> messagesByWebSocket, UIUpdater uiUpdater) {
        this.messagesByWebSocket = messagesByWebSocket;
        this.uiUpdater = uiUpdater;
    }

    @Override
    public List<Component> provideMenuItems(WebSocketContextMenuEvent event) {
        JMenuItem menuItem = new JMenuItem("Send to WebSocket Messages Tab");
        menuItem.addActionListener(e -> {
            List<WebSocketMessage> selectedMessages = event.selectedWebSocketMessages();
            
            for (WebSocketMessage message : selectedMessages) {
                MessageData historicalMessage = new MessageData(
                        message.direction().toString(),
                        message.payload(),
                        false,
                        message.payload().length(),
                        new Date()
                );
                messagesByWebSocket.get("Historical Messages").add(historicalMessage);
            }
            
            uiUpdater.notifyDataChanged();
            uiUpdater.notifyMapChanged();
        });
        return List.of(menuItem);
    }
}
