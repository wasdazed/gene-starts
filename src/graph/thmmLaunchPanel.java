package graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import thmm.Launch;

public class thmmLaunchPanel extends JPanel{
	
	public thmmLaunchPanel(final String f){
	
	this.setLayout(new BorderLayout());
		
	final JTextArea text = new JTextArea();
	
	JPanel buttons = new JPanel();
	buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	
	final JTextField alpha = new JTextField("0.01");
	JLabel alphaL = new JLabel("alpha:");
	final JTextField lambda = new JTextField("1");
	JLabel lambdaL = new JLabel("lambda:");
	
	final JButton launch = new JButton("launch THMM");
	launch.setName("launch");
	launch.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			try{
				double a;
				try{
					a = Double.parseDouble(alpha.getText());
				}
				catch(NumberFormatException ex1) {
						JOptionPane.showMessageDialog(null, "can't parse alpha");
						return;
				}
				double l = -1;
				try{
					l = Double.parseDouble(lambda.getText());
				}
				catch(NumberFormatException ex1) {}
				String res = Launch.thmmString(f,a,l);
				text.setText(res);
			}
			catch(Exception x){
				JOptionPane.showMessageDialog(null, "Exception in thmm...");
			}				
		}
	});	
	buttons.add(alphaL);
	buttons.add(alpha);
	buttons.add(lambdaL);
	buttons.add(lambda);
	buttons.add(launch);
			
	text.scrollRectToVisible(getBounds());
	text.setFont(new Font("monospaced", Font.PLAIN, 18));
	text.append("my thmm results");

	JScrollPane scrollPane = new JScrollPane(text);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setPreferredSize(new Dimension(1500, 250));

	this.add(buttons,BorderLayout.NORTH);
	this.add(scrollPane, BorderLayout.CENTER);

	
	}
}
