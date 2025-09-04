import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

public class WebSocketTableModel extends AbstractTableModel {
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private Object currentWebSocket;
    private final String[] columnNames = {"Direction", "Message", "Binary", "Length", "Timestamp"};

    public WebSocketTableModel(Map<Object, List<MessageData>> messagesByWebSocket) {
        this.messagesByWebSocket = messagesByWebSocket;
    }

    public void setCurrentWebSocket(Object webSocket) {
        this.currentWebSocket = webSocket;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        if (currentWebSocket == null) return 0;
        List<MessageData> messages = messagesByWebSocket.get(currentWebSocket);
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (currentWebSocket == null) return null;
        List<MessageData> messages = messagesByWebSocket.get(currentWebSocket);
        if (messages == null || row >= messages.size()) return null;

        MessageData msg = messages.get(row);
        switch (column) {
            case 0: return msg.getDirection();
            case 1: return msg.getMessage().toString();
            case 2: return msg.isBinary();
            case 3: return msg.getLength();
            case 4: return msg.getTimestamp();
            default: return null;
        }
    }

    public void notifyDataChanged() {
        fireTableDataChanged();
    }
}