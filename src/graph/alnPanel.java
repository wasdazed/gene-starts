package graph;

import java.awt.BorderLayout;
import javax.swing.JPanel;


import aln.Alignment;

public class alnPanel extends JPanel{

	
	public alnPanel(Alignment a,String f,int upstream) {
		
		thmmLaunchPanel thmm = new thmmLaunchPanel(f);
		
		AlnPanelItself aln = new AlnPanelItself(a,upstream);
		
		this.setLayout(new BorderLayout(100,100));
		this.add(aln,BorderLayout.NORTH);
   		this.add(thmm,BorderLayout.CENTER);
   	}
}
