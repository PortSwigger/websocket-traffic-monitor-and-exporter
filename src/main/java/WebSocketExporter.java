import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketExporter implements BurpExtension {
    
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("Hello");
        Logging logging = montoyaApi.logging();
        
        Map<Object, List<MessageData>> messagesByWebSocket = new HashMap<>();
        messagesByWebSocket.put("Historical Messages", new ArrayList<>());
        
        MyWebSocketMessageTab tab = new MyWebSocketMessageTab(logging, messagesByWebSocket);
        UIUpdater uiUpdater = new UIUpdater(tab.getTableModel(), tab.getComboBoxModel());
        
        MyWebSocketCreatedHandler handler = new MyWebSocketCreatedHandler(logging, messagesByWebSocket, uiUpdater);
        MyContextMenuItemsProvider contextMenu = new MyContextMenuItemsProvider(messagesByWebSocket, uiUpdater);
        
        montoyaApi.userInterface().registerSuiteTab("WebSocketMessages", tab);
        montoyaApi.websockets().registerWebSocketCreatedHandler(handler);
        montoyaApi.userInterface().registerContextMenuItemsProvider(contextMenu);
    }
}
