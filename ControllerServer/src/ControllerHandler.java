import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Thread to handle communications with a client
 * @author RohanCollins
 */
class ControllerHandler extends Thread 
{ 
	
	private final ObjectInputStream 			objinput;
	private final ObjectOutputStream 			objoutput;
	
	private final Socket 						socket;
	private final File     						csv;
	private String 								clientType;
	
	/**
	 * Thread to read the sensor data and send it to the client
	 */
	private final Thread dataReadingThread = new Thread() {
		
		@Override
		public void run() {
			
			try {
				
				CsvReader reader = new CsvReader(csv);
				
				while(!this.isInterrupted()) {
					
					SensorReading reading = reader.readNextLine();

					switch(clientType) {
					
					case "TEMP":
						
						objoutput.writeObject(new ControllerMessage("READING", 0, ""+reading.getTemperature()));
						break;
						
					case "LIGHT":
						
						objoutput.writeObject(new ControllerMessage("READING", 0, ""+reading.getLightLevel()));
						break;
					}

					Thread.sleep(reading.calcWaitTime());
				}
				
				System.out.println("DataReadingThread ended");
				reader.close();
				
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
			
		}
	};

	/**
	 * ControllerHandler constructor
	 * @param s
	 * @param csv
	 * @throws IOException 
	 */
	public ControllerHandler(Socket s, File csv) throws IOException 
	{ 
		this.socket = s; 
		this.objoutput = new ObjectOutputStream(s.getOutputStream());
		this.objinput = new ObjectInputStream(s.getInputStream());
		this.csv = csv;
		
		System.out.println("Handler created"); 
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
						
						dataReadingThread.interrupt();
						disconnect();
						connected = false; 
						break; 
						
					case "ANSWER_TYPE":
						clientType = received.getMessage();
						System.out.println("Type set");
						dataReadingThread.start();
						break; 
					}
				}
			} 		
			
		} catch (IOException | ClassNotFoundException e) { 
			e.printStackTrace(); 
		} 
	} 
	
	/**
	 * Close connection to client
	 */
	private void disconnect() throws IOException {
		
		System.out.println("Client " + this.socket + " sends exit..."); 
		System.out.println("Closing this connection."); 
		this.socket.close();
		this.objinput.close(); 
		this.objoutput.close(); 
		
		System.out.println("Connection closed"); 
	}
	
	/**
	 * 
	 * @return Message received from client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private ControllerMessage readStream() throws ClassNotFoundException, IOException {
		
		ControllerMessage received = (ControllerMessage) this.objinput.readObject();
		System.out.println("Msg recived: " + received.toText()); 
		
		return received;
	}
	
	/**
	 * Sends request to see if the client is a temperature or light controller
	 * @throws IOException
	 */
	private void requestType() throws IOException {
		
		System.out.println("Type requested"); 
		this.objoutput.writeObject(new ControllerMessage("REQUEST_TYPE", 0, "Welcome"));
	}
} 