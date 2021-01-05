
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	final 	File					csv;
	
	private ObjectOutputStream 		objoutput;
	private ObjectInputStream 		objinput;
	
	private ServerSocket 			server_socket;
	
	public Server(int port) {
		
		//Store reference to sensor data file
		this.csv = new File("D:\\Documents\\eclipse-workspace\\ControllerServer\\sensor_data.csv");
		
		try {
			
			this.server_socket = new ServerSocket(port);
			System.out.println("## server is listening to port: " + port + " waiting for client connections..");
			
			//Loop to listen for clients requesting to connect to the server
			while (true)
			{ 
				Socket s = null;
				
				try
				{ 
					s = this.server_socket.accept(); 
					
					System.out.println("A new client is connected : " + s); 
					
					objoutput = new ObjectOutputStream(s.getOutputStream()); 
					objinput = new ObjectInputStream(s.getInputStream()); 
					
					System.out.println("Assigning new thread for this client"); 

					Thread t = new ControllerHandler(s, objinput, objoutput, csv);
					
					System.out.println("Starting thread"); 
					t.start();
					
				}
				catch (Exception e){ 

					s.close(); 
					objinput.close();
					objoutput.close();
					e.printStackTrace(); 	
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		} 	
	}
	
	public static void main(String[] args) {
		// server is listening on port 5056 as previous example port changed from 3142
		
		int port = Integer.parseInt(args[0]);		
		@SuppressWarnings("unused")
		Server server = new Server(port);		
		
	}
}

/**
 * Thread to handle communications with a client
 * @author RohanCollins
 */
class ControllerHandler extends Thread 
{ 
	
	final ObjectInputStream 			objinput;
	final ObjectOutputStream 			objoutput;
	
	final Socket 						s;
	final File     						csv;
	String 								type;
	
	/**
	 * Thread to read the sensor data and send it to the client
	 */
	private Thread DataReadingThread = new Thread() {
		
		@Override
		public void run() {
			
			try {
				
				CsvReader reader = new CsvReader(csv);
				
				while(!this.isInterrupted()) {
					
					SensorReading reading = reader.readNextLine();
					
					String data;
					
					if(type.equals("TEMP")) {
						
						data = ""+reading.getTemperature();
						
					} else {
						
						data = ""+reading.getLightLevel();
					}

					objoutput.writeObject(new ControllerMessage(type + "_READING", 0, data));
					
					Thread.sleep(reading.calcWaitTime());
				}
				
				System.out.println("DataReadingThread ended");
				reader.close();
				
			} catch (IOException | InterruptedException e) { 
				e.printStackTrace(); 
			}
			
		}
	};
	
	/**
	 * ControllerHandler constructor
	 * @param s
	 * @param objinput
	 * @param objoutput
	 * @param csv
	 */
	public ControllerHandler(Socket s, ObjectInputStream objinput, ObjectOutputStream objoutput, File csv) 
	{ 
		this.s = s; 
		this.objinput = objinput; 
		this.objoutput = objoutput; 
		this.csv = csv;
	} 

	@Override
	/**
	 * run method, called when a client handler thread is starting..
	 * handles client requests
	 */
	public void run() 
	{ 
		try { 
			
			boolean connected = true;
			requestType();
		
			while (connected) 
			{ 
				ControllerMessage received = readStream();
				
				if(received != null) {
					
					System.out.println(received.toText());
					
					switch(received.getType()) {
					
					case "STOP":
						
						DataReadingThread.interrupt();
						disconnect();
						connected = false; 
						break; 
						
					case "ANSWER_TYPE":
						type = received.getMessage();
						System.out.println("Type set");
						DataReadingThread.start();
						break; 
					}
				}
			} 
			
			objinput.close(); 
			objoutput.close(); 		
			
		} catch (IOException | ClassNotFoundException e) { 
			e.printStackTrace(); 
		} 
	} 
	
	/**
	 * Close connection to client
	 */
	private void disconnect() throws IOException {
		
		System.out.println("Client " + this.s + " sends exit..."); 
		System.out.println("Closing this connection."); 
		s.close();
		System.out.println("Connection closed"); 
	}
	
	/**
	 * 
	 * @return Message received from client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private ControllerMessage readStream() throws ClassNotFoundException, IOException {
		
		ControllerMessage received = (ControllerMessage) objinput.readObject();
		System.out.println("Msg recived: " + received.toText()); 
		
		return received;
	}
	
	/**
	 * Sends request to see if the client is a temperature or light controller
	 * @throws IOException
	 */
	private void requestType() throws IOException {
		
		System.out.println("Type requested"); 
		objoutput.writeObject(new ControllerMessage("REQUEST_TYPE", 0, "Welcome"));
	}
} 