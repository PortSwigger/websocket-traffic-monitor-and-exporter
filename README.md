# WebSocket Traffic Monitor & Exporter

A comprehensive Burp Suite extension for monitoring, capturing, and exporting WebSocket traffic across all Burp tools.

## Features

### 🔍 **Real-time WebSocket Monitoring**
- Captures WebSocket messages from **all Burp tools** (Proxy, Repeater, Scanner, Intruder, etc.)
- Real-time display of incoming and outgoing messages
- Support for both text and binary message types
- Intelligent capture control with enable/disable toggle

### 📊 **Smart Message Management**
- **Tool Source Tracking**: See which Burp tool each WebSocket originated from
- **Numbered WebSocket Organization**: Each connection gets a unique ID for easy identification  
- **Smart Filtering**: Only shows WebSockets that actually received messages
- **Historical Message Support**: Right-click context menu integration

### 🎯 **User-Friendly Interface**
- **Split-pane layout**: Message table on left, payload viewer on right
- **Scrollable dropdown**: Organized by newest connections first
- **Click-to-view**: Click any message row to view full payload
- **Responsive UI**: Real-time updates as messages flow

### 📁 **Advanced CSV Export**
- **Smart filename generation**: Auto-suggests `ID-URL-TOOL.csv` format
- **Complete message data**: Direction, content, type, length, timestamps
- **Burp-consistent timestamps**: `HH:mm:ss dd MMM yyyy` format
- **CSV-safe content**: Automatic escaping of special characters

## Installation

1. Download the latest release JAR file
2. In Burp Suite, go to **Extensions → Installed → Add**
3. Select the JAR file and click **Next**
4. The extension will appear as "**WebSocket Messages**" in the main tab bar

## Usage

### Basic Monitoring
1. Navigate to the **"WebSocket Messages"** tab
2. Ensure **"Enable Message Capture"** is checked
3. Use any Burp tool to interact with WebSocket endpoints
4. Messages appear automatically in the table

### Dropdown Format
WebSockets appear as: `ID. URL - TOOL`
- Example: `1. wss://example.com/ws - Burp Proxy`
- **Historical Messages** (from context menu) appear at the top
- Newest WebSockets appear first (reverse chronological order)

### Message Export
1. Select a WebSocket from the dropdown
2. Click **"Export to CSV"**
3. Choose destination folder
4. Default filename: `1-wss___example.com_ws-Burp_Proxy.csv`
5. CSV includes: Direction, Message, Binary, Length, Timestamp

### Capture Control
- **Enable/Disable**: Toggle message capture without losing existing data
- **Background tracking**: WebSockets opened while disabled are tracked but not shown
- **Smart activation**: Re-enabling capture shows new traffic from previously opened connections

## Technical Details

### Supported Message Types
- ✅ Text messages (JSON, XML, plain text)
- ✅ Binary messages (with length tracking)
- ✅ Both incoming and outgoing directions

### Tool Integration
- **Proxy**: Intercepted WebSocket traffic
- **Repeater**: Manual WebSocket testing
- **Scanner**: Automated security scanning
- **Intruder**: Bulk WebSocket attacks
- **Context Menu**: Historical message analysis

### Performance
- Efficient dual-map architecture for optimal UI responsiveness
- Background tracking minimizes memory usage
- Real-time updates without blocking the interface

## Requirements

- **Burp Suite Professional/Community** (2023.1 or later)
- **Java**: JDK 11 or higher
- Uses **Burp Montoya API** for modern extension capabilities

## Architecture

The extension uses a clean separation of concerns:
- **WebSocketWrapper**: Encapsulates connection metadata (ID, URL, tool source)
- **MessageData**: Immutable message representation with proper timestamp formatting
- **Dual Maps**: Background tracking vs. visible UI data
- **Generic Processing**: Unified handling for text/binary messages

## Building from Source

```bash
git clone <repository-url>
cd BurpExtensionWebSocketExporter
./gradlew build
```

The compiled JAR will be in `build/libs/`