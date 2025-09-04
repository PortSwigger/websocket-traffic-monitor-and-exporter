import javax.swing.SwingUtilities;

public class UIUpdater {
    private final WebSocketTableModel tableModel;
    private final WebSocketComboBoxModel comboBoxModel;
    
    public UIUpdater(WebSocketTableModel tableModel, WebSocketComboBoxModel comboBoxModel) {
        this.tableModel = tableModel;
        this.comboBoxModel = comboBoxModel;
    }
    
    public void notifyDataChanged() {
        tableModel.notifyDataChanged();
    }
    
    public void notifyMapChanged() {
        SwingUtilities.invokeLater(() -> comboBoxModel.notifyMapChanged());
    }
}