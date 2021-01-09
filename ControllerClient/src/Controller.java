import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public abstract class Controller {
	
	protected final String 						type;
	
	protected final ObjectOutputStream 			objoutput;
	protected final ObjectInputStream 			objinput;
	protected final Socket 						socket;
	protected final String						ipaddress;
	protected final int							portnumber;

	protected String 							userInput;
	
	/**
	 * Thread to listen for user input
	 */
	protected final Thread userInputThread = new Thread() {
		public void run() {
			
			Scanner scn = new Scanner(System.in); 
			
			while(true) {
				
				userInput = scn.nextLine();
				
				System.out.println("User inputted:" + userInput);
				
				if(userInput.equals("STOP")) 
				{ 
					System.out.println("UserInputThread ended");
					scn.close();
					break; 
				}
			}	
		}
	};
	
	/**
	 * Controller constructor
	 * @param ipaddress
	 * @param port
	 * @param type
	 * @throws Exception  
	 */
	public Controller(String ipaddress, int port, String type) throws Exception {
		
		this.type = type;
		this.ipaddress = ipaddress;
		this.portnumber = port;
		this.userInput = "";
		this.userInputThread.start();

		//connect to server
		this.socket = new Socket(this.ipaddress, this.portnumber);
		this.objoutput = new ObjectOutputStream(this.socket.getOutputStream()); 
		this.objinput = new ObjectInputStream(this.socket.getInputStream()); 

		System.out.println("Set up complete");
				
	}
	
	protected void start() throws Exception {
		
		while (true) 
		{ 
			ControllerMessage received = readStream();
			
			if(userInput.equals("STOP")) 
			{ 
				disconnect();
				break; 
			}
			else {
				
				switch(received.getType()){
				
				case "REQUEST_TYPE": 
					answerType();
					break; 
					
				case "READING":
					handleReading(Float.parseFloat(received.getMessage()));
					break;
					
				}
			}	
			
			userInput = "";
		}
		
		socket.close(); 
		objinput.close(); 
		objoutput.close(); 
	}
	
	protected abstract void handleReading(float value);
	
	/**
	 * Disconnect from server
	 * @throws IOException
	 */
	protected void disconnect() throws IOException {
		 
		System.out.println("Closing this connection : " + this.socket); 
		System.out.println("Connection closed"); 
		objoutput.writeObject(new ControllerMessage("STOP", 0, ""));
	}
	
	/**
	 * Forward controller type to server
	 * @throws IOException
	 */
	protected void answerType() throws IOException {
		
		objoutput.writeObject(new ControllerMessage("ANSWER_TYPE", 0, type));
		System.out.println("Type sent");
	}
	
	
	/**
	 * @return Message received from server
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	protected ControllerMessage readStream() throws ClassNotFoundException, IOException {
		
		ControllerMessage received = (ControllerMessage) objinput.readObject();
		System.out.println("Msg recived: " + received.toText()); 
		
		return received;
	}
	
	public static void main(String[] args) {

		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		String type = args[2];
		
		@SuppressWarnings("unused")
		Controller client;
		
		try {
		
		switch (type) {
		
		case "TEMP":

			client = new TempController(ip, port);
			break;
			
		case "LIGHT":
			
			client = new LightController(ip, port);
			break;
		}
		
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
}