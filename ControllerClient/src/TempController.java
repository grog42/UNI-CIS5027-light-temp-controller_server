
import fan.FanDisplay;

/**
 * Controller specifically to handle temperature readings from server 
 * @author RohanCollins
 *
 */
public class TempController extends Controller{

	private FanDisplay 						fan;
	
	public TempController(String ipaddress, int port) throws Exception {
		
		super(ipaddress, port, "TEMP");
		this.fan = new FanDisplay();
		this.start();
	}
	
	/**
	 * Reacts to a temperature reading by adjusting the fan speed
	 */
	@Override
	public void handleReading(float value) {
					
		fan.setSpeed(value);
	}
}