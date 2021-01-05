/**
 * 
 * @author thanu (original author - uknown)
 * modified by thanuja
 * 
 */

package fan;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

public class Fan {
	
	private final int triWidth = 20, triHeight = 40; //Wing triangle variables
	private final int arcWidth = 20, arcHeight = 10; //Wing arc variables

	private int centerX, centerY; //The center of the fan
	private double angel; //Angel in radians
		
	private	int fanSpeed;
	private FanPanel fan_panel;

	public void setFanJPanel(FanPanel fanJPanel) {
		this.fan_panel = fanJPanel;
	}
	
	public int getFanSpeed() {
		return fanSpeed;
	}
	
	public void setFanSpeed(int speed) {
		this.fanSpeed = speed;
		this.fan_panel.setTimer(this.fanSpeed);
	}
	
	public Fan(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public void update() {
		angel = addRad(angel, 0.1);
	}
	
	private double addRad(double current, double addition) {
		double value = current + addition;
		
		if (value >= 2 * Math.PI) 
			value -= 2 * Math.PI;
		return value;
	}

	public void draw(Graphics g) {
		Graphics2D gx = (Graphics2D) g;
		//Make it look a little prettier
		gx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Setting up triangle.
		//We only want to do the calculations for the top wing. We rotate the top wing to get the other four wings.
		int[] x = { centerX + triWidth / 2, centerX - triWidth / 2, centerX };
		int[] y = { centerY - triHeight, centerY - triHeight, centerY };
		Polygon triangle = new Polygon(x, y, x.length);
		
		//Rotate 'angel' radians around the center
		gx.rotate(angel, centerX, centerY);
		
		for (int i = 0; i < 4; i++) {
			//Draw the wing arc (top)
			//Start with an angel of 0 for starting horizontal left to right. Let the arc have an angel of 180
			gx.fillArc(centerX - triWidth / 2, centerY - triHeight - arcHeight / 2, arcWidth, arcHeight, 0, 180);

			//Draw the wing triangle
			gx.fillPolygon(triangle);
			
			//Rotate on top of the existing rotation. Rotate 45 degrees
			gx.rotate(Math.PI / 2, centerX, centerY);
		}
	}
}
