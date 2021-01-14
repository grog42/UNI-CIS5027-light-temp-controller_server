import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Stores data read from the csv file
public class SensorReading {
	
	private Date time;
	
	private String tmnode1;
	private String appId;
	private String hexPayload;
	private String event;
	private String protocol;
	
	private int id;
	private int deviceId;
	private int counter;
	private int battery;
	private int lightLevel;
	
	private double timeStamp;
	
	private float temperature;
	private float frequency;

	public SensorReading(String fileLine){
		
		String[] params = fileLine.split(",");
		
		id = Integer.parseInt(params[0].replace("(", ""));
		tmnode1 = params[1];
		appId = params[2];
		deviceId = Integer.parseInt(params[3]);
		counter = Integer.parseInt(params[4]);
		hexPayload = params[5];
		battery = Integer.parseInt(params[6]);
		lightLevel = Integer.parseInt(params[7]);
		event = params[8];
		temperature = Float.parseFloat(params[9]);
		
		try {
			time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS").parse(params[10].replace("'", "").replace("Z", "").trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frequency = Float.parseFloat(params[11]);
		protocol = params[12];
		timeStamp = Double.parseDouble(params[13]);
	}
	
	//Calculate in milliseconds the time until the next reading
	public long calcWaitTime() {
		return Math.round(frequency);
	}

	public int getId() {
		return id;
	}

	public String getTmnode1() {
		return tmnode1;
	}

	public int getDeviceId() {
		return deviceId;
	}
	
	public String appId() {
		return appId;
	}

	public int getCounter() {
		return counter;
	}

	public String getHexPayload() {
		return hexPayload;
	}

	public int getBattery() {
		return battery;
	}

	public int getLightLevel() {
		return lightLevel;
	}

	public String getEvent() {
		return event;
	}

	public float getTemperature() {
		return temperature;
	}

	public Date getTime() {
		return time;
	}

	public float getFrequency() {
		return frequency;
	}

	public String getProtocol() {
		return protocol;
	}

	public double getTimeStamp() {
		return timeStamp;
	}
}
