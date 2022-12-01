package contact.helper;

public class Message {
	private String messageType;
	private String messageDesc;
	
	public Message(String messageType, String messageDesc) {
		super();
		this.messageType = messageType;
		this.messageDesc = messageDesc;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageDesc() {
		return messageDesc;
	}
	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}
	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", messageDesc=" + messageDesc + "]";
	}
	
}
