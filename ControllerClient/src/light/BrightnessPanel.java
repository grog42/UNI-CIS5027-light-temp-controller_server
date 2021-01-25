package light;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import light.Light;

/**
 * 
 * @author RohanCollins
 *
 */
public class BrightnessPanel extends JPanel{
	
	private static final long 	serialVersionUID = 1L;
	private JLabel 				lbl_brightness_value;
	
	public BrightnessPanel(Light lightObj) {
		
		this.lbl_brightness_value 	= new JLabel("Brightness:");
		this.setLayout(new FlowLayout());
		this.add(lbl_brightness_value);
	}
	
	public void setBrightnessValue(float value) {
		
		this.lbl_brightness_value.setText("Brightness:" + value);
	}
}