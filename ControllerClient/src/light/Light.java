package light;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

public class Light {
		
	private	float brightness;
	private LightPanel panel;

	public void setJPanel(LightPanel JPanel) {
		this.panel = JPanel;
	}
	
	public float getBrightness() {
		return brightness;
	}
	
	public void setBrightness(float brightness) {
		
		this.brightness = brightness;
		this.panel.repaint();
	}
	
	public Light() {
		
		this.brightness = 0;
	}

	public void draw(Graphics g) {
		Graphics2D gx = (Graphics2D) g;
		//Make it look a little prettier
		gx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		gx.setColor(new Color(brightness/255, brightness/255, brightness/255));
		
		gx.fillRect(0, 0, panel.getWidth(), panel.getHeight());
	}
	
}
