import java.io.Serializable;

/**
 * A data object for transferring messages between client and server
 * @author RohanCollins
 *
 */
public class SocketMessage implements Serializable {
	
	private String 		type;
	private String 		message;
	
	private int 		flags;
	
	public SocketMessage(String type, int flags, String message){
		
		this.type = type;
		this.flags = flags;
		this.message = message;
	}
	
	//Returns a string displaying all the data within the message
	public String toText() {
		
		return "\r\nType: " + type + "\nFlags: " + flags + "\nMessage: " + message + "\r";
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getFlags() {
		return this.flags;
	}
	
	public String getMessage() {
		return this.message;
	}
}