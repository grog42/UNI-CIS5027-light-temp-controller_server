import java.awt.BorderLayout;

import fan.Fan;
import fan.FanPanel;
import fan.SpeedPanel;
import light.BrightnessPanel;
import light.Light;
import light.LightPanel;

public class LightDisplay extends EnvironmentalDisplay {

	private LightPanel 			light_panel;
	private BrightnessPanel		brightness_panel;
	private Light				light_instance;
	
	private float					brightness;
	
	public LightDisplay(){
		super();
		
		light_instance = new Light();
		
		light_panel = new LightPanel(300, 300, light_instance);
		brightness_panel = new BrightnessPanel(light_instance);
				
		add(brightness_panel, BorderLayout.NORTH);
		add(light_panel, BorderLayout.CENTER);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		handleReading(0);
	}
	@Override
	public void handleReading(float value) {
		
		brightness = value;
		
		light_instance.setBrightness(brightness);
		brightness_panel.setBrightnessValue(brightness);
	}

}
