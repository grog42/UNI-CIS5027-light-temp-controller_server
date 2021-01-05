import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import fan.FanDisplay;

public class Controller {

	private ObjectOutputStream 		objoutput;
	private ObjectInputStream 		objinput;
	
	private Socket 					s;
	
	private FanDisplay 				fan;
	
	private String					ipaddress;
	private String					type;
	private String 					userInput;
	
	private int						portnumber;
	
	/**
	 * Set up thread to listen for user input
	 */
	private Thread userInputThread = new Thread() {
		public void run() {
			
			Scanner scn = new Scanner(System.in); 
			
			while(!this.isInterrupted()) {
				userInput = scn.nextLine();
			}	
			
			System.out.println("UserInputThread ended");
			scn.close();
		}
	};
	
	/**
	 * Controller constructor
	 * @param ipaddress
	 * @param port
	 * @param type
	 */
	public Controller(String ipaddress, int port, String type) {
		
		this.ipaddress = ipaddress;
		this.portnumber = port;
		this.type = type;
		this.userInput = "";
		this.userInputThread.start();
		this.fan = new FanDisplay();

		try {

			connect();
			
			while (true) 
			{ 
				ControllerMessage received = readStream();
				
				System.out.println(userInput);
					
				if(userInput.equals("STOP")) 
				{ 
					userInputThread.interrupt();
					disconnect();
					break; 
				}
				else {
					
					switch(received.getType()){
					
					case "REQUEST_TYPE": 
						answerType();
						break; 
						
					case "LIGHT_READING":
						break; 
						
					case "TEMP_READING":
						
						float temp = Float.parseFloat(received.getMessage());			
						fan.setSpeed(temp);
						break; 
					}
				}	
				
				userInput = "";
			}
			
			s.close(); 
			objinput.close(); 
			objoutput.close(); 
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Establish connection to server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void connect() throws UnknownHostException, IOException {
		
		s = new Socket(this.ipaddress, this.portnumber);
		objinput = new ObjectInputStream(s.getInputStream()); 
		objoutput = new ObjectOutputStream(s.getOutputStream()); 
		
		System.out.println("Set up complete");
	}
	
	/**
	 * Disconnect from server
	 * @throws IOException
	 */
	private void disconnect() throws IOException {
		 
		System.out.println("Closing this connection : " + s); 
		System.out.println("Connection closed"); 
		objoutput.writeObject(new ControllerMessage("STOP", 0, ""));
	}
	
	/**
	 * Forward controller type to server
	 * @throws IOException
	 */
	private void answerType() throws IOException {
		
		objoutput.writeObject(new ControllerMessage("ANSWER_TYPE", 0, type));
		System.out.println("Type sent");
	}
	
	
	/**
	 * @return Message received from server
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private ControllerMessage readStream() throws ClassNotFoundException, IOException {
		
		ControllerMessage received = (ControllerMessage) objinput.readObject();
		System.out.println("Msg recived: " + received.toText()); 
		
		return received;
	}
		
	public static void main(String[] args) {

		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		String type = args[2];
		
		@SuppressWarnings("unused")
		Controller client = new Controller(ip, port, type);

	}
}