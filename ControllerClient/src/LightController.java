
public class LightController extends Controller{

	protected final String 			type = "LIGHT";
	
	public LightController(String ipaddress, int port) throws Exception {
		
		super(ipaddress, port, "LIGHT");
		this.start();
	}

	@Override
	protected void handleReading(float value) {
		// TODO Auto-generated method stub
		
	}
}