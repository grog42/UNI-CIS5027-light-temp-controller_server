package fan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @adapted-by RohanCollins
 *
 */
public class FanPanel extends JPanel implements ActionListener {
	
	private Fan 	fan;
	private Timer 	timer;
	
	private final static int DEFAULT_DELAY_TIME = 10;

	public FanPanel(int width, int height, Fan fanObj) {
		setPreferredSize(new Dimension(width, height));		
		fan = fanObj;
		fan.setFanJPanel(this);
		timer = new Timer(DEFAULT_DELAY_TIME, this); //Update our animations every n ms
		timer.setInitialDelay(DEFAULT_DELAY_TIME * 7);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		fan.draw(g);
	}
	
	public Fan getFanInstance() {
		return this.fan;
	}
	
	public void setTimer(int delay) {		
		timer.setDelay(delay);
        timer.setInitialDelay(delay * 10);
		timer.restart();
	}
	
	/**
	 * this function is called, everytime the timer goes off (in this case 3 ms). 
	 *
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		fan.update(); //Let the fan move
		repaint(); //Draw the changes
	}
}