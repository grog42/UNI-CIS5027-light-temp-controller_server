/**
 * Object used to share a string between threads
 * @author RohanCollins
 *
 */
public class SharedString {

	private String str;
	
	public SharedString(String str){
		this.str = str;
	}
	
	public synchronized void put(String str) {
		this.str = str;
	}
	
	public synchronized String get() {
		return this.str;
	}
}
