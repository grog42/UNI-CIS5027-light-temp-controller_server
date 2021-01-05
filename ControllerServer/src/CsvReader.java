
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CsvReader extends Thread{

	final Scanner reader;
	
	public CsvReader(File file) throws FileNotFoundException {
		
		reader = new Scanner(file);
		reader.nextLine();
		
	}
	
	public void close() {
		
		reader.close();
	}
		
	public SensorReading readNextLine() {
		
		String line;
		
		if((line = reader.nextLine()) == null) {
			
			System.out.println("Reader at end");
			reader.reset();
			reader.nextLine();
			line = reader.nextLine();

		}
		
		return new SensorReading(line);
	}
	
}