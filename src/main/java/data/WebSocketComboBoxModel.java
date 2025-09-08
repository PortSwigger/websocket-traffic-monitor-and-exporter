package data;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class WebSocketComboBoxModel extends AbstractListModel<Object> implements ComboBoxModel<Object> {
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private Object selectedItem;

    public WebSocketComboBoxModel(Map<Object, List<MessageData>> messagesByWebSocket) {
        this.messagesByWebSocket = messagesByWebSocket;
    }

    public void notifyMapChanged() {
        fireContentsChanged(this, 0, Math.max(0, getSize() - 1));
    }

    @Override
    public int getSize() {
        return messagesByWebSocket.size();
    }

    @Override
    public Object getElementAt(int index) {
        return getSortedKeys().get(index);
    }
    
    private List<Object> getSortedKeys() {
        List<Object> keys = new ArrayList<>(messagesByWebSocket.keySet());
        
        List<Object> historicalMessages = keys.stream()
            .filter(key -> key.equals("Historical Messages"))
            .collect(Collectors.toList());
            
        List<WebSocketWrapper> wrappers = keys.stream()
            .filter(key -> key instanceof WebSocketWrapper)
            .map(key -> (WebSocketWrapper) key)
            .sorted((w1, w2) -> Integer.compare(w2.getNumber(), w1.getNumber()))
            .collect(Collectors.toList());
        
        List<Object> result = new ArrayList<>();
        result.addAll(historicalMessages);
        result.addAll(wrappers);
        
        return result;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.selectedItem = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
}