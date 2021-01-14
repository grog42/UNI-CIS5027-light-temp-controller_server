
import java.io.IOException;

/**
 * Controller specifically to handle temperature readings from server 
 * @author RohanCollins
 *
 */
public class TempController extends Controller{

	public TempController(String ipaddress, int port) throws IOException {
		
		super(ipaddress, port, "TEMP", new FanDisplay());
	}
}