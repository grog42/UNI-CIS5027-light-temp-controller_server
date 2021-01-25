import java.awt.BorderLayout;

import fan.Fan;
import fan.FanPanel;
import fan.SpeedPanel;


/**
 * Provides a visual display of a fan with an alternating speed based on supplied temperature.
 * @author RohanCollins
 *
 */
public class FanDisplay extends EnvironmentalDisplay{

	private FanPanel 		fan_panel;
	private SpeedPanel		speed_panel;
	private Fan				fan_instance;
	
	private int				speed;
	
	/**
	 * Constructor
	 */
	public FanDisplay() {
		super();
		
		fan_instance = new Fan(150, 150);
		
		fan_panel = new FanPanel(300, 300, fan_instance);
		speed_panel = new SpeedPanel(fan_instance);
				
		add(speed_panel, BorderLayout.NORTH);
		add(fan_panel, BorderLayout.CENTER);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		handleReading(0);
	}
	
	/**
	 * Reacts to a temperature reading by adjusting the fan speed
	 * The speed works via a feedback loop; If the temperature is greater than room temperature (25C)
	 * Than the speed is increased by 1 until at maximum speed (0)
	 * Else the fan is slowed by one until at minimum speed (10)
	 */
	@Override
	public void handleReading(float temp) {
		
		if(temp > 25) {
			speed--;
			
			if(speed < 0) {speed = 0;}
			
		} else {
			speed++;
			
			if(speed > 10) {speed = 10;}
		}
		
		fan_instance.setFanSpeed(speed);
		speed_panel.setSpeedValue(speed);
		
	}

}
