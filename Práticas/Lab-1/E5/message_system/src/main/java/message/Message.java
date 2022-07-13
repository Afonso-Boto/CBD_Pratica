package message;

import java.time.LocalDate;
import java.util.Map;

public class Message {
    private String id;
    private String content;
    private String sender;
    private String receiver;

    public static final String SYSTEM_MESSAGE = "SYSTEM_MESSAGE";
    public static final String SYSTEM_MESSAGE_ID = "SYSTEM_MESSAGE_ID";
    public static int idCounter = 0;


    public Message( String content, String sender, String receiver) {
        this.id = String.valueOf(++idCounter);
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public static Message instanceOf(String content, String sender, String receiver) {
        return new Message(content, sender, receiver);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Map<String, String> toMap(){
        return Map.of("id", id, "content", content, "sender", sender, "receiver", receiver);
    }


    @Override
    public String toString() {
        return "MESSAGE [%s] from %s to %s: %s".format(String.valueOf(LocalDate.now()), sender, receiver, content);
    }

}
