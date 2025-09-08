package burp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import data.MessageData;
import data.UIUpdater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketExporter implements BurpExtension {

    
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("WebSocket Exporter");
        Logging logging = montoyaApi.logging();
        
        Map<Object, List<MessageData>> allWebSocketsMap = new HashMap<>();
        
        Map<Object, List<MessageData>> visibleWebSocketsMap = new HashMap<>();
        visibleWebSocketsMap.put("Historical Messages", new ArrayList<>());
        
        MyWebSocketMessageTab tab = new MyWebSocketMessageTab(logging, montoyaApi.userInterface(), visibleWebSocketsMap);
        UIUpdater uiUpdater = new UIUpdater(tab.getTableModel(), tab.getComboBoxModel());
        
        MyWebSocketCreatedHandler handler = new MyWebSocketCreatedHandler(logging, allWebSocketsMap, visibleWebSocketsMap, uiUpdater, tab);
        MyContextMenuItemsProvider contextMenu = new MyContextMenuItemsProvider(visibleWebSocketsMap, uiUpdater);
        
        montoyaApi.userInterface().registerSuiteTab("WebSocketMessages", tab);
        montoyaApi.websockets().registerWebSocketCreatedHandler(handler);
        montoyaApi.userInterface().registerContextMenuItemsProvider(contextMenu);
    }
}
