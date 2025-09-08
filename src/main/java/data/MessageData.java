package data;

import burp.api.montoya.core.ByteArray;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageData {
    private final String direction;
    private final ByteArray message;
    private final boolean isBinary;
    private final int length;
    private final Date timestamp;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMM yyyy");

    public MessageData(String direction, ByteArray message, boolean isBinary, int length, Date timestamp) {
        this.direction = direction;
        this.message = message;
        this.isBinary = isBinary;
        this.length = length;
        this.timestamp = timestamp;

    }

    public String getDirection() {
        return direction;
    }
    public ByteArray getMessage() {
        return message;
    }
    public boolean isBinary() {
        return isBinary;
    }
    public int getLength() {
        return length;
    }

    public String getTimestamp() {
        return dateFormat.format(timestamp);
    }
}
