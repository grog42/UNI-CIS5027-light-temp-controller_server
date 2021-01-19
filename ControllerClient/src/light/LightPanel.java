package light;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class LightPanel extends JPanel {
	
	private Light 	light;

	public LightPanel(int width, int height, Light lightObj) {
		setPreferredSize(new Dimension(width, height));		
		light = lightObj;
		light.setJPanel(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		light.draw(g);
	}
	
	public Light getLightInstance() {
		return this.light;
	}
}