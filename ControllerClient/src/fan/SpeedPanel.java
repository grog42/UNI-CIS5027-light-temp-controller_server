package fan;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SpeedPanel extends JPanel{
	
	private static final long 	serialVersionUID = 1L;
	private JLabel 				lbl_speed_value;
	
	public SpeedPanel(Fan fanObj) {
		
		this.lbl_speed_value 	= new JLabel("Fan speed (Delay in ms) :");
		this.setLayout(new FlowLayout());
		this.add(lbl_speed_value);
	}
	
	public void setFanInstance(Fan fanInstance) {
		
	}
	
	public void setSpeedValue(int speed) {
		
		this.lbl_speed_value.setText("Fan speed (Delay in ms) :" + speed);
	}
}