import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class UserInterface extends JFrame{

	JButton stopButton = new JButton("Stop");
	Controller controller;
	
	public UserInterface(Thread currentThread) {
		super();
		
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
	
	public void handleReading() {
		
	}
	
}
