import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * A objects which acts as a client to communicate with the server for enviromental data
 * @author RohanCollins
 *
 */
public abstract class Controller {
	
	protected final String 						type;
	
	protected final ObjectOutputStream 			objoutput;
	protected final ObjectInputStream 			objinput;
	protected final Socket 						socket;
	protected final String						ipaddress;
	protected final int							portnumber;
	protected String 							userInput;
	protected Thread 							mainThread;
	protected EnvironmentalDisplay				userInterface;
	
	/**
	 * Thread to listen for user input
	 */
	protected final Thread userInputThread = new Thread() {
		
		@Override
		public void run() {
			
			Scanner scn = new Scanner(System.in); 
			
			while(!this.isInterrupted()) {
				
				userInput = scn.nextLine();
				
				System.out.println("User inputted:" + userInput);
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
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws Exception  
	 */
	public Controller(String ipaddress, int port, String type, EnvironmentalDisplay userInterface) throws IOException {
		
		System.out.println("Controller set up...");
		
		this.type = type;
		this.ipaddress = ipaddress;
		this.portnumber = port;
		this.userInput = "";
		this.userInterface = userInterface;

		//connect to server
		this.socket = new Socket(this.ipaddress, this.portnumber);
		this.objoutput = new ObjectOutputStream(this.socket.getOutputStream()); 
		this.objinput = new ObjectInputStream(this.socket.getInputStream()); 

		System.out.println("Controller set up complete");
		
		Thread mainThread = Thread.currentThread();
		
		//Starts the communications loop with the server'

			this.userInputThread.start();
			
			while (!mainThread.isInterrupted()) 
			{ 
				try {
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
							userInterface.handleReading(Float.parseFloat(received.getMessage()));
							break;
						
						case "STOP":
							answerStop();

							break;
						
						}
					}	
				
					userInput = "";
				
				} 
				catch(IOException | ClassNotFoundException e) {
					System.out.println("Message recived error; Moving to next message");
					
				}
			}
		
		disconnect();
				
	}
	
	/**
	 * Disconnect from server
	 * @throws IOException
	 */
	protected void disconnect() {
		 
		try {

			System.out.println("Closing this connection : " + this.socket); 
			System.out.println("Connection closed"); 
			objoutput.writeObject(new ControllerMessage("STOP", 0, ""));
			
			userInputThread.interrupt();
			socket.close(); 
			objinput.close(); 
			objoutput.close();
			userInterface.dispose();
		
		} catch (IOException e) {

			e.printStackTrace();
			
		} 
	}
	
	/**
	 * Forward controller type to server
	 * @throws IOException
	 */
	protected void answerType() throws IOException {
		
		objoutput.writeObject(new ControllerMessage("ANSWER_TYPE", 0, type));
		System.out.println("Type sent");
	}
	
	protected void answerStop() throws IOException {
		
		objoutput.writeObject(new ControllerMessage("STOP", 0, ""));
		System.out.println("Stop sent");
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
		Scanner scn = new Scanner(System.in); 
		
		//Ask the user which kind of controller they want
		while(true) {
			try {
				System.out.println("Please enter the type of controller you would like to start up ('TEMP' or 'LIGHT')"); 
			
				switch (scn.nextLine()) {
				
				case "TEMP":

					scn.close();
					new TempController(ip, port);
					return;
					
				case "LIGHT":
					
					scn.close();
					new LightController(ip, port);
					return;
					
				default:
					
					System.out.println("Input was not 'TEMP or 'LIGHT'"); 
						
				}
			}	
			catch(IOException e) {
			
			System.out.println("Controller set up failed try again");
			e.printStackTrace(); 	

			}
		}
	}
}