package graph2;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JPanel;

import prodigal.ProdigalRecord;
import thmm.ScoresMapperMO;
import tree.NewickString;
import tree.reader.NewickStringReader;
import aln.Alignment;

public class alnPanel extends JPanel{

	
	public alnPanel(String af,String tf,String prodigal) throws NumberFormatException, IOException {
		
		Alignment a = Alignment.readAlignmentFromFile(af);
		
		Hashtable<Integer,ProdigalRecord>[] scoresHash = new Hashtable[a.names.size()];
		
		AlnPanelItself alnPanel = new AlnPanelItself(a,prodigal,scoresHash);
		
		NewickString ns = NewickStringReader.readFromFile(tf);
		
		Hashtable<String,double[]> h = new Hashtable<String, double[]>();
		String[] stateNames = ScoresMapperMO.getMapping(a, h, scoresHash,alnPanel.starts,alnPanel.strands);
		
		thmmLaunchPanel thmm = new thmmLaunchPanel(a,ns,h,stateNames);		
		
		this.setLayout(new BorderLayout(100,5));
		this.add(alnPanel,BorderLayout.NORTH);
   		this.add(thmm,BorderLayout.CENTER);
   	}
}
