package graph2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import aln.Alignment;
import thmm.Launch;
import thmm.LaunchMO;
import tree.NewickString;

public class thmmLaunchPanel extends JPanel{
	
	public thmmLaunchPanel(Alignment aln,final NewickString ns,final Hashtable<String, double[]> h, final String[] stateNames){
	
	this.setLayout(new BorderLayout());
		
	final JTextArea text = new JTextArea();
	
	JPanel buttons = new JPanel();
	buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	
	final JTextField alpha = new JTextField("0.01");
	JLabel alphaL = new JLabel("alpha:");
	final JTextField lambda = new JTextField("1");
	JLabel lambdaL = new JLabel("lambda:");
	
	StringBuilder sb = new StringBuilder();
			
	text.scrollRectToVisible(getBounds());
	text.setFont(new Font("monospaced", Font.PLAIN, 18));
	sb.append("=====================TREE=========================\n");
	sb.append(ns.getNewickString()+"\n");
//	text.append("treeee\n");
	
	sb.append("=================STATES (ALN POSITIONS)=================\n");
//	text.append("\t");
	for (int i = 0; i < stateNames.length; i++) {
		sb.append("\t"+stateNames[i]);
	}
	sb.append("\n");
	
	sb.append("=================SCORE HASHTABLE h=================\n");
	for (int j = 0; j < aln.names.size(); j++) {
		String string = aln.names.get(j).substring(1);
		double [] e = h.get(string);
//		System.out.println(string);
//		text.append(string);
		for (int i = 0; i < e.length; i++) {
			sb.append("\t"+e[i]);
		}
		sb.append("\n");
	}	
	
	final String header = sb.toString();
	text.append(header);

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
//				String res = Launch.thmmString(f,a,l);
				String res = LaunchMO.thmmString(ns,stateNames,h,a,l);
				text.setText(header);
				text.append(res);
			}
			catch(Exception x){
				x.printStackTrace();
				JOptionPane.showMessageDialog(null, "Exception in thmm..."+x.getMessage());
			}				
		}
	});	
	buttons.add(alphaL);
	buttons.add(alpha);
	buttons.add(lambdaL);
	buttons.add(lambda);
	buttons.add(launch);
	
	
	JScrollPane scrollPane = new JScrollPane(text);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setPreferredSize(new Dimension(1500, 250));

	this.add(buttons,BorderLayout.NORTH);
	this.add(scrollPane, BorderLayout.CENTER);

	
	}
}
