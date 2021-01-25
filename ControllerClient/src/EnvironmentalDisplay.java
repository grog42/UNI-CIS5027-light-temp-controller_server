import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Class used to offer a visual display for the environmental controllers
 * @author RohanCollins
 *
 */
public abstract class EnvironmentalDisplay extends JFrame{

	protected JButton stopButton = new JButton("Stop");
	
	protected SharedString userInput;
	
	protected EnvironmentalDisplay(){
		
		stopButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {

				userInput.put("STOP");
			}});
		
		add(stopButton, BorderLayout.NORTH);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Link access to the user input string
	 * @param userInput
	 */
	public void linkUserInput(SharedString userInput) {
		
		this.userInput = userInput;
	}
	
	/**
	 * Taking an environmental reading as an input this function determines how the GUI should change based on the input
	 * @param value
	 */
	public abstract void handleReading(float value);
}
