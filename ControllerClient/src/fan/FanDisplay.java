package fan;


import java.awt.BorderLayout;
import javax.swing.JFrame;

public class FanDisplay extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// static instance to support singleton
	private static FanDisplay	fanui_instance;
	
	private FanPanel 		fan_panel;
	private SpeedPanel		speed_panel;
	private Fan				fan_instance;
	
	private int				speed;
	
	/**
	 * Public getinstance method to create an instance of the AppFrame class. 
	 *  
	 * @return an instance of AppFrame class. 
	 */
	public static FanDisplay getFanUIInstance() {
		if(fanui_instance == null) {
			fanui_instance = new FanDisplay();
		}
		
		return fanui_instance;
	}
	
	public void setSpeed(float temp) {
		
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
		
		setSpeed(0);
	}
}