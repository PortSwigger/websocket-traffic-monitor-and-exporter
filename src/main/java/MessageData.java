import burp.api.montoya.core.ByteArray;

public class MessageData {
    private final String direction;
    private final ByteArray message;
    private final boolean isBinary;
    private final int length;
    private final String timestamp;

    public MessageData(String direction, ByteArray message, boolean isBinary, int length, String timestamp) {
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
        return timestamp;
    }
}
