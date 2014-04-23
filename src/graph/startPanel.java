package graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import aln.Alignment;

public class startPanel extends JPanel{
	
//	public static final String dir = "/home/koshka/work/GENE_START/";
	public static final String dir = "";
	
	tabs t;
	
	public startPanel(final tabs t){
		this.t=t;		
		
	/*	String name1 = "cases_ecoliW3110_ecoliDH10B/less/muscle/";
		JPanel mp1 = getFilePanel(name1);
		this.add(mp1);
		
		String name2 = "cases_ecoliW3110_ecoliDH10B/more/muscle/";
		JPanel mp2 = getFilePanel(name2);
		this.add(mp2);*/
		
		String name3 = "cases_ecoliW3110_ecoliDH10B_ecoliO157I_shigella_salmonella/less/muscle/";
//		JPanel mp3 = getFilePanel(name3);
		JScrollPane mp3 = getFileScrollPane(name3);
		this.add(mp3);
		
	}

	private JPanel getFilePanel(String name){
		JPanel mp1 = new JPanel();
		mp1.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));
		mp1.setLayout(new BorderLayout());
		JLabel l = new JLabel(name);
		l.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		l.setBackground(Color.CYAN);
		mp1.add(l,BorderLayout.NORTH);
		JScrollPane p1 = getFileScrollPane(dir+name);
		mp1.add(p1);
		return mp1;
	}
	
	private JScrollPane getFileScrollPane(String dir){
		File f = new File(dir);
		File[] ff = f.listFiles();
		Arrays.sort(ff);		
		
		JPanel p1= new JPanel();
		p1.setLayout(new GridLayout((int)(ff.length/5)+1, 5,5, 5));
		p1.setAlignmentX(CENTER_ALIGNMENT);
	
		for (int i = 0; i < ff.length; i++) {
			final JButton seeAln = new JButton(ff[i].getName());
			seeAln.setName(ff[i].getAbsolutePath());
			seeAln.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try{
						t.addAlnTab(Alignment.readAlignmentFromFile(seeAln.getName()),seeAln.getName());
					}
					catch(NumberFormatException x){
						JOptionPane.showMessageDialog(null, "no aln");
					}				
				}
			});	
			p1.add(seeAln);
		}
		JScrollPane p= new JScrollPane(p1);
		p.setPreferredSize(new Dimension(1200, 800));
		p.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);;
		return p;
	}
	
}
