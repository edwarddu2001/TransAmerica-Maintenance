import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ErrorMessage extends JFrame implements ActionListener {
	
	
	private JPanel contentPane = new JPanel();
	private JButton button = new JButton("Exit");
	private JLabel error = new JLabel("Error: Not enough players.");
	
	
	ErrorMessage() {
		this.setTitle("Error");
		button.addActionListener(this);
		
		contentPane.setLayout(new GridLayout(1,2));
		contentPane.add(error);
		contentPane.add(button);
		
		
		
		this.add(contentPane);
		this.setLocation(650,375);
		this.setSize(300, 150);
		this.setResizable(false);
		this.setVisible(true);
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button)) {
			this.setVisible(false);
		}
	}

}
