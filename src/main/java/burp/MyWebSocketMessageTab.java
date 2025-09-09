package burp;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.WebSocketMessageEditor;
import data.MessageData;
import data.WebSocketComboBoxModel;
import data.WebSocketTableModel;
import data.WebSocketWrapper;
import utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MyWebSocketMessageTab extends JPanel {
    private final Map<Object, List<MessageData>> messagesByWebSocket;
    private final Logging logging;
    private final UserInterface userInterface;
    private JComboBox<Object> webSocketDropdown;
    private WebSocketTableModel tableModel;
    private WebSocketComboBoxModel comboBoxModel;
    private JCheckBox captureEnabledCheckBox;

    public MyWebSocketMessageTab(Logging logging, UserInterface userInterface, Map<Object, List<MessageData>> messagesByWebSocket) {
        setLayout(new BorderLayout());
        this.messagesByWebSocket = messagesByWebSocket;
        this.logging = logging;
        this.userInterface = userInterface;
        addTable();
    }

    private void addTable() {
        comboBoxModel = new WebSocketComboBoxModel(messagesByWebSocket);
        tableModel = new WebSocketTableModel(messagesByWebSocket);
        
        webSocketDropdown = new JComboBox<>(comboBoxModel);
        webSocketDropdown.setMaximumRowCount(10);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        WebSocketMessageEditor webSocketMessageEditor = userInterface.createWebSocketMessageEditor(EditorOptions.READ_ONLY);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Object currentWebSocket = webSocketDropdown.getSelectedItem();
                    if (currentWebSocket != null) {
                        List<MessageData> messages = messagesByWebSocket.get(currentWebSocket);
                        if (messages != null && selectedRow < messages.size()) {
                            MessageData selectedMessage = messages.get(selectedRow);
                            webSocketMessageEditor.setContents(selectedMessage.getMessage());
                        }
                    }
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, webSocketMessageEditor.uiComponent());
        splitPane.setResizeWeight(0.6);

        captureEnabledCheckBox = new JCheckBox("Enable Message Capture", true);
        captureEnabledCheckBox.addActionListener(e -> comboBoxModel.notifyMapChanged());


        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportToCSV());

        webSocketDropdown.addActionListener(e -> {
            Object selectedWebSocket = webSocketDropdown.getSelectedItem();
            tableModel.setCurrentWebSocket(selectedWebSocket);
        });

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(captureEnabledCheckBox);
        topPanel.add(webSocketDropdown);
        topPanel.add(exportButton);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }


    private void exportToCSV() {
        logging.logToOutput("Export button clicked!");
        Object selected = webSocketDropdown.getSelectedItem();
        logging.logToOutput("Selected WebSocket: " + selected);
        List<MessageData> messages = messagesByWebSocket.get(selected);
        logging.logToOutput("Messages to export: " + (messages != null ? messages.size() : 0));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            String defaultFilename = (selected instanceof WebSocketWrapper) ? 
                                  ((WebSocketWrapper) selected).getCsvFilename() + ".csv" : "websocket_export.csv";
            String filename = JOptionPane.showInputDialog(this, "Enter filename:", defaultFilename);
            if (filename == null) return;
            if (!filename.endsWith(".csv")) {
                filename = filename + ".csv";
            }
            File csvFile = new File(selectedFolder, filename);
            try (FileWriter writer = new FileWriter(csvFile)) {
                writer.write("Websocket: " + selected + "\n");
                writer.write("Direction,Message,Binary,Length,Timestamp\n");
                if (messages != null) {
                    for (MessageData msg : messages) {
                        String processedMessage = Utilities.escapeString(msg.getMessage().toString());
                        writer.write(msg.getDirection() + "," +
                                processedMessage + "," +
                                msg.isBinary() + "," +
                                msg.getLength() + "," +
                                msg.getTimestamp() + "\n");
                    }
                }
            } catch (IOException e) {
                logging.logToError("Export failed: " + e.getMessage());
            }
        }
    }

    public WebSocketTableModel getTableModel() {
        return tableModel;
    }

    public boolean isCaptureEnabled() {
        return captureEnabledCheckBox.isSelected();
    }

    public WebSocketComboBoxModel getComboBoxModel() {
        return comboBoxModel;
    }




}
