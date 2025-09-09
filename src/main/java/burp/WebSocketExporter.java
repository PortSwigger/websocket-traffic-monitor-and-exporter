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
    public static final String EXTENSION_NAME = "WebSocket Traffic Monitor & Exporter";

    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName(EXTENSION_NAME);
        Logging logging = montoyaApi.logging();
        
        Map<Object, List<MessageData>> allWebSocketsMap = new HashMap<>();
        Map<Object, List<MessageData>> visibleWebSocketsMap = new HashMap<>();
        visibleWebSocketsMap.put("Historical Messages", new ArrayList<>());
        
        MyWebSocketMessageTab tab = new MyWebSocketMessageTab(logging, montoyaApi.userInterface(), visibleWebSocketsMap);
        UIUpdater uiUpdater = new UIUpdater(tab.getTableModel(), tab.getComboBoxModel());
        
        MyWebSocketCreatedHandler handler = new MyWebSocketCreatedHandler(allWebSocketsMap, visibleWebSocketsMap, uiUpdater, tab);
        MyContextMenuItemsProvider contextMenu = new MyContextMenuItemsProvider(visibleWebSocketsMap, uiUpdater);
        
        montoyaApi.userInterface().registerSuiteTab("WebSocket Messages", tab);
        montoyaApi.websockets().registerWebSocketCreatedHandler(handler);
        montoyaApi.userInterface().registerContextMenuItemsProvider(contextMenu);
    }
}
