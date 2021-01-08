
import fan.FanDisplay;

public class TempController extends Controller{

	private FanDisplay 						fan;
	
	public TempController(String ipaddress, int port) throws Exception {
		
		super(ipaddress, port, "TEMP");
		this.fan = new FanDisplay();
	}
	
	@Override
	public void handleReading(float value) {
					
		fan.setSpeed(value);
	}

}