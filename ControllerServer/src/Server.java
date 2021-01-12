
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Server which connects to clients and provides environmental data
 * @author RohanCollins
 *
 */
public class Server {
	
	private final File				csv;

	private ServerSocket 			server_socket;
	
	private ControllerHandler[]		handlers;
	
	private String 					userInput;
	
	protected final Thread socketAcceptingThread = new Thread() {
		
		@Override
		public void run() {
			
			//Loop to listen for clients requesting to connect to the server
			while (!this.isInterrupted())
			{ 
				Socket socket = null;
				
				try {
					
					socket = server_socket.accept(); 
					
					System.out.println("A new client is connected : " + socket); 
					System.out.println("Assigning Handler for this client"); 
					
					if(!(handlers[0] != null)) {
						
						addHandler(0, socket);
						
					} else if(!(handlers[1] != null)) {
						
						addHandler(1, socket);
						
					} else {
						
						System.out.println("Server only allows for 2 connections"); 
						
					}
				
				}
				catch (IOException e){ 

					e.printStackTrace(); 	
				}
			}
		}
		
	};
	
	public Server(int port, File csv) throws IOException {
		
		//Store reference to sensor data file
		this.csv = csv;
		this.server_socket = new ServerSocket(port);
		this.userInput = "";
		this.handlers = new ControllerHandler[2];
		
		System.out.println("## server is listening to port: " + port + " waiting for client connections..");
		socketAcceptingThread.start();
		
		listenForUserInput();
		
		removeHandler(0);
		removeHandler(1);
		server_socket.close();
		csv.deleteOnExit();
	}
	
	private void listenForUserInput() {
		
		Scanner scn = new Scanner(System.in);
		
		while(!userInput.equals("STOP")) {
			
			userInput = scn.nextLine();		
			
			System.out.println("User inputted:" + userInput);
			
		}
		
		System.out.println("User input suspended");
		scn.close();
	}
	
	private void addHandler(int index, Socket socket) throws IOException {
		
		//A new thread is created to handle the client
		handlers[index] = new ControllerHandler(socket, csv);
		handlers[index].start();
		System.out.println("Handler running");
	}
	
	private void removeHandler(int index) {
		
		if(!handlers[index].equals(null)) {
			
			handlers[index].interrupt();
			handlers[index] = null;
		}
	}
	
	public static void main(String[] args) {

		int port = Integer.parseInt(args[0]);
		Scanner scn = new Scanner(System.in); 
		
		try {
			
			while(true) {
				
				System.out.println("Please enter the file path of the csv else leave blank for default"); 
				
				String input = scn.nextLine();
				File csv = new File(input);
				
				if(csv.exists()) {
					
					new Server(port, csv);
					break;
				}
				
				if(input.isBlank()) {
					new Server(port, new File("D:\\Documents\\eclipse-workspace\\ControllerServer\\sensor_data.csv"));
					break;
				}
					
				System.out.println("File not found");
			}
			
		} catch (Exception e) {

			System.out.println("Server startup failed");
			e.printStackTrace();
		}		
			
		scn.close();
		
	}
}