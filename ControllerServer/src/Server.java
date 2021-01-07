
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private final File						csv;

	private ServerSocket 			server_socket;
	
	public Server(int port) throws Exception {
		
		//Store reference to sensor data file
		this.csv = new File("D:\\Documents\\eclipse-workspace\\ControllerServer\\sensor_data.csv");
		this.server_socket = new ServerSocket(port);
		
		
		System.out.println("## server is listening to port: " + port + " waiting for client connections..");
		
		//Loop to listen for clients requesting to connect to the server
		while (true)
		{ 
			Socket socket = null;
			
			try
			{ 
				socket = this.server_socket.accept(); 
				
				System.out.println("A new client is connected : " + socket); 
				System.out.println("Assigning Handler for this client"); 

				//A new thread is created to handle the client
				Thread t = new ControllerHandler(socket, csv);
				t.start();
				System.out.println("Handler running"); 
				
			}
			catch (Exception e){ 

				socket.close(); 
				e.printStackTrace(); 	
			}
		}
	}
	
	public static void main(String[] args) {
		// server is listening on port 5056 as previous example port changed from 3142
		
		int port = Integer.parseInt(args[0]);
		
		try {

			@SuppressWarnings("unused")
			Server server = new Server(port);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}