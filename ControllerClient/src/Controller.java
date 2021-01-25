import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * A objects which acts as a client to communicate with the server for environmental data
 * @author RohanCollins
 *
 */
public abstract class Controller implements SocketCommunicator{
	
	protected final String 						type;
	
	protected final ObjectOutputStream 			objoutput;
	protected final ObjectInputStream 			objinput;
	protected final Socket 						socket;
	protected final String						ipaddress;
	protected final int							portnumber;
	protected EnvironmentalDisplay				gui;
	protected SharedString 						userInput;
	protected boolean							active;
	
	/**
	 * Thread for listening for user to input stop
	 * @throws IOException 
	 */
	protected final Thread userInputThread = new Thread() {
		
		@Override
		public void run() {
			
			Scanner scn = new Scanner(System.in);
			
			while(!this.interrupted()) {
				
				userInput.put(scn.nextLine());	
				
				System.out.println("User inputted:" + userInput);
				
			}
			
			System.out.println("User input suspended");
			
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
		this.gui = userInterface;
		
		this.userInput = new SharedString("");
		this.gui.linkUserInput(userInput);

		//connect to server
		this.socket = new Socket(this.ipaddress, this.portnumber);
		this.objoutput = new ObjectOutputStream(this.socket.getOutputStream()); 
		this.objinput = new ObjectInputStream(this.socket.getInputStream()); 
		
		this.active = true;

		System.out.println("Controller set up complete");

		//Starts the communications loop with the server'
		userInputThread.start();
		
		while (!userInput.get().equals("STOP")) {

			try {
				SocketMessage received = readStream(objinput);
			
				switch(received.getType()){
				
				case "REQUEST_TYPE": 
					
					tellType();
					break; 
				
				case "READING":
					
					gui.handleReading(Float.parseFloat(received.getMessage()));
					break;
				
				case "STOP":
					
					userInput.put("STOP");
					break;
				
				}
			
			} 
			catch(IOException | ClassNotFoundException e) {
				
				System.out.println("Message recived error; Moving to next message");
			}
		}
		
		active = false;
			
		disconnect();
				
	}
	
	/**
	 * Disconnect from server
	 * @throws IOException
	 */
	@Override
	public void disconnect() {
		 
		try {

			System.out.println("Closing this connection : " + this.socket); 

			
			userInputThread.interrupt();
			System.out.println("Press enter to end user input");
			
			try {
				userInputThread.join();
				
			} catch (InterruptedException e1) {

			}
			
			try {
				
				this.tellStop(objoutput);
				this.objinput.close(); 
				this.objoutput.close();
				this.socket.close(); 
			
			} catch (SocketException e) {
				System.out.println("Failed: Connection alread closed. Proceeding...");
			}
			
			System.out.println("Connection closed"); 
			
			gui.dispose();
			System.out.println("GUI dissposed"); 
		
		} catch (IOException e) {

			e.printStackTrace();
			
		} 
	}
	
	/**
	 * Forward controller type to server
	 * @throws IOException
	 */
	protected void tellType() throws IOException {
		
		objoutput.writeObject(new SocketMessage("ANSWER_TYPE", 0, type));
		System.out.println("Type sent");
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

					new TempController(ip, port);
					return;
					
				case "LIGHT":
					
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