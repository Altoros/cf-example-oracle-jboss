package jdbc;

/**
 * @author: Andrey Kozlov
 */
public class MessageData {

    private int count;
    private int lastMessage;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(int lastMessage) {
        this.lastMessage = lastMessage;
    }
}
