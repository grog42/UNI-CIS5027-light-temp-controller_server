import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public interface SocketCommunicator {
	
	public abstract void disconnect();
	
	default void tellStop(ObjectOutputStream objoutput) throws IOException, SocketException {
		
		System.out.println("Informing socket of shutdown"); 
		objoutput.writeObject(new SocketMessage("STOP", 0, "Handler thread shutting down"));
		
	}
	
	/**
	 * 
	 * @return Message received from client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	default SocketMessage readStream(ObjectInputStream objinput) throws IOException, ClassNotFoundException {
		
		SocketMessage received;

		received = (SocketMessage) objinput.readObject();		
		System.out.println("Msg recived: " + received.toText()); 
			
		return received;
	}
}