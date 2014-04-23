package graph2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import graph.alnPanel;
import aln.Alignment;

public class CasesPanel extends JPanel{
	
	public static String MOG_alnDir = "/home/koshka/work/GENE_START/V2_MO/MO_MOG_aln_selected";
//	public static String MOG_alnDir = "MO_MOG_aln_selected";
	public static String MOG_treeDir = "/home/koshka/work/GENE_START/V2_MO/MO_MOG_selected_trees";
//	public static String MOG_treeDir = "MO_MOG_selected_trees";
	
	public static String COG_alnDir;
	public static String COG_treeDir;
	
	public static String prodigalDir = "/home/koshka/work/GENE_START/V2_MO/prodigal_res";
//	public static String prodigalDir = "prodigal_res";
		
	public static CasesPanel getMOGCasesPanel(tabs t){
		return new CasesPanel(MOG_alnDir,MOG_treeDir,prodigalDir,t);
	}
	
	public static CasesPanel getCOGCasesPanel(tabs t){
		return new CasesPanel(COG_alnDir,COG_treeDir,prodigalDir,t);
	}
	
	public CasesPanel(String alnDir,final String treesDir,final String prodigal_res,final tabs t) {
		File f = new File(alnDir);
		File[] ff = f.listFiles();
		Arrays.sort(ff);		
			
		JPanel p1= new JPanel();
		p1.setLayout(new GridLayout((int)(ff.length/5)+1, 5,5, 5));
		for (int i = 0; i < ff.length; i++) {
			final JButton seeAln = new JButton(ff[i].getName());
			final String alnName = ff[i].getAbsolutePath();
			final String treeName =treesDir+alnName.substring(alnName.lastIndexOf("/"))+".nwk";			
			seeAln.setName(alnName);
			seeAln.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try{
						t.addAlnTab(alnName,treeName,prodigal_res);
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(null,"io:"+e1.getMessage());
						e1.printStackTrace();
					}				
				}
			});	
			p1.add(seeAln);
		}
		JScrollPane p= new JScrollPane(p1);
		p.setPreferredSize(new Dimension(1200, 800));
		p.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		this.setLayout(new BorderLayout());
		this.add(p,BorderLayout.CENTER);
	}		
}
