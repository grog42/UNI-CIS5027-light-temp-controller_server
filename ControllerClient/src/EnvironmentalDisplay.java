import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public abstract class EnvironmentalDisplay extends JFrame{

	protected JButton stopButton = new JButton("Stop");
	
	protected EnvironmentalDisplay(){
		
		Thread currentThread = Thread.currentThread();
		
		stopButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {

				currentThread.interrupt();
			}});
		
		add(stopButton, BorderLayout.NORTH);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public abstract void handleReading(float value);
}
