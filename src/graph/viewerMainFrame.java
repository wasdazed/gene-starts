package graph;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class viewerMainFrame extends JFrame{
	
	public viewerMainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		tabs t = new tabs();
		this.setLayout(new BorderLayout());
		this.getContentPane().add(t,BorderLayout.CENTER);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);		
	}

	public static void main(String[] args) {
		viewerMainFrame mf = new viewerMainFrame();
	}
	
}
