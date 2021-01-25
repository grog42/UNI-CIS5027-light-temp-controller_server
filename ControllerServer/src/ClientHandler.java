import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Thread to handle server communications with a client
 * @author RohanCollins
 */
class ClientHandler extends Thread implements SocketCommunicator 
{ 
	
	private final ObjectInputStream 			objinput;
	private final ObjectOutputStream 			objoutput;
	
	private final Socket 						socket;
	private final File     						csv;
	private String 								clientType;
	private boolean								active;
	
	/**
	 * Thread to read the sensor data and send it to the client
	 */
	private final Thread dataReadingThread = new Thread() {
		
		@Override
		public void run() {
			
			try {
				
				CsvReader reader = new CsvReader(csv);
				
				while(active) {
					
					SensorReading reading = reader.readNextLine();

					switch(clientType) {
					
					case "TEMP":
						
						objoutput.writeObject(new SocketMessage("READING", 0, ""+reading.getTemperature()));
						break;
						
					case "LIGHT":
						
						objoutput.writeObject(new SocketMessage("READING", 0, ""+reading.getLightLevel()));
						break;
					}

					Thread.sleep(reading.calcWaitTime());
					System.out.println("Delay to next reading: " + reading.calcWaitTime());
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
	 * @param csv
	 * @throws IOException 
	 */
	public ClientHandler(Socket socket, File csv) throws IOException 
	{ 
		System.out.println("Handler set up...");
		
		this.socket = socket; 
		this.objoutput = new ObjectOutputStream(socket.getOutputStream());
		this.objinput = new ObjectInputStream(socket.getInputStream());
		this.csv = csv;
		this.active = true;

		System.out.println("Handler set up complete");
	} 

	@Override
	/**
	 * run method, called when a client handler thread is starting..
	 * handles client requests
	 */
	public void run()
	{ 
		System.out.println("Handler starting"); 
		try { 
			
			requestType();
		
			while (active) 
			{ 
				SocketMessage received = readStream(objinput);
				
				if(received != null) {
					
					System.out.println(received.toText());
					
					switch(received.getType()) {
					
					case "STOP":
						
						this.active = false;
						break; 
						
					case "ANSWER_TYPE":
						clientType = received.getMessage();
						System.out.println("Type set");
						dataReadingThread.start();
						break; 
					}
				}
			} 		
			
		} catch (Exception e) { 

			System.out.println("Exception detected closing connection"); 
		} 
		
		disconnect();
	} 
	
	public void close() {
		this.active = false;
	}
	
	/**
	 * Close connection to client
	 */
	@Override
	public void disconnect() {
		
		try {

			System.out.println("Client " + this.socket + " sends exit..."); 
			System.out.println("Closing this connection."); 

			try {
				
				dataReadingThread.join();
				
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
			
			try {
				
				this.tellStop(objoutput);
				this.objinput.close(); 
				this.objoutput.close(); 
				this.socket.close();
			
			} catch (SocketException e) {
				System.out.println("Socket alread closed proceeding");
			}
			
			System.out.println("Connection closed. Waiting for new connection..."); 
		
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * Sends request to see if the client is a temperature or light controller
	 * @throws IOException
	 */
	private void requestType() throws IOException {
		
		System.out.println("Type requested"); 
		this.objoutput.writeObject(new SocketMessage("REQUEST_TYPE", 0, "Welcome"));
	}
	
} 