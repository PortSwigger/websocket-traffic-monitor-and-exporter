import javax.swing.*;
import java.util.List;
import java.util.Map;

public class WebSocketComboBoxModel extends AbstractListModel<Object> implements ComboBoxModel<Object> {
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private Object selectedItem;

    public WebSocketComboBoxModel(Map<Object, List<MessageData>> messagesByWebSocket) {
        this.messagesByWebSocket = messagesByWebSocket;
    }

    public void notifyMapChanged() {
        fireContentsChanged(this, 0, getSize() - 1);
    }

    @Override
    public int getSize() {
        return messagesByWebSocket.size();
    }

    @Override
    public Object getElementAt(int index) {
        return messagesByWebSocket.keySet().toArray()[index];
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