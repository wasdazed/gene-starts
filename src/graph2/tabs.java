package graph2;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import aln.Alignment;

public class tabs extends JTabbedPane{
	
	public tabs(){}
	
	private JPanel makeTabExitPanel(String name, final JComponent jc){
		JButton exitTabButton = new JButton("x");
		exitTabButton.setMargin(new Insets(0,0,0,0));
		exitTabButton.setPreferredSize(new Dimension(15,15));
		exitTabButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				remove(jc);
			}
		});
		JPanel tabPanel = new JPanel();
		JLabel l = new JLabel(name);
		l.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		tabPanel.add(l);
		tabPanel.add(exitTabButton);
		tabPanel.setOpaque(false);
		return tabPanel;
	}
	
	public void addAlnTab(String af,String tree,String prodigal) throws NumberFormatException, IOException{
		alnPanel ap = new alnPanel(af,tree,prodigal);
		String name = af.substring(af.lastIndexOf("/")+1);
		this.addTab(name,ap);
		int i = indexOfComponent(ap);
		JPanel exitTabPanel = makeTabExitPanel(name,ap); 
		this.setTabComponentAt(i, exitTabPanel);
	}
	
}	
	