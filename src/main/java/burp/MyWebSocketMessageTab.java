package burp;

import burp.api.montoya.logging.Logging;
import data.MessageData;
import data.WebSocketComboBoxModel;
import data.WebSocketTableModel;

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
    private JComboBox<Object> webSocketDropdown;
    private WebSocketTableModel tableModel;
    private WebSocketComboBoxModel comboBoxModel;

    public MyWebSocketMessageTab(Logging logging, Map<Object, List<MessageData>> messagesByWebSocket) {
        setLayout(new BorderLayout());
        this.messagesByWebSocket = messagesByWebSocket;
        this.logging = logging;
        addTable();
    }

    private void addTable() {
        comboBoxModel = new WebSocketComboBoxModel(messagesByWebSocket);
        tableModel = new WebSocketTableModel(messagesByWebSocket);
        
        webSocketDropdown = new JComboBox<>(comboBoxModel);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

//        JButton refreshButton = new JButton("Refresh");
//        refreshButton.addActionListener(e -> {
//            Object currentSelection = webSocketDropdown.getSelectedItem();
//            logging.logToOutput("Map size: " + messagesByWebSocket.size());
//            logging.logToOutput("Map keys: " + messagesByWebSocket.keySet());
//            webSocketDropdown.removeAllItems();
//            for (Object ws : messagesByWebSocket.keySet()) {
//                webSocketDropdown.addItem(ws);
//            }
//            webSocketDropdown.setSelectedItem(currentSelection);
//        });

        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportToCSV());

        webSocketDropdown.addActionListener(e -> {
            Object selectedWebSocket = webSocketDropdown.getSelectedItem();
            tableModel.setCurrentWebSocket(selectedWebSocket);
        });

        JPanel topPanel = new JPanel(new FlowLayout());
//        topPanel.add(refreshButton);
        topPanel.add(webSocketDropdown);
        topPanel.add(exportButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
            String filename = JOptionPane.showInputDialog(this, "Enter filename:", "websocket_export.csv");
            if (filename == null) return;
            if (!filename.endsWith(".csv")) {
                filename = filename + ".csv";
            }
            File csvFile = new File(selectedFolder, filename);
            try (FileWriter writer = new FileWriter(csvFile)) {
                writer.write("Websocket: " + selected + "\n");
                writer.write("Direction,Message,Binary,Length,Timestamp\n");
                for (MessageData msg : messages) {
                    writer.write(msg.getDirection() + "," +
                            msg.getMessage().toString() + "," +
                            msg.isBinary() + "," +
                            msg.getLength() + "," +
                            msg.getTimestamp() + "\n");
                }
            } catch (IOException e) {
                logging.logToError("Export failed: " + e.getMessage());
            }
        }
    }


    public JComboBox<Object> getWebSocketDropdown() {
        return webSocketDropdown;
    }

    public WebSocketTableModel getTableModel() {
        return tableModel;
    }

    public WebSocketComboBoxModel getComboBoxModel() {
        return comboBoxModel;
    }




}
