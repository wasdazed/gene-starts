package graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import thmm.ScoresMapper;
import aln.Alignment;

public class AlnPanelItself extends JPanel{
	
	private static String makeBenchmarkNumbers(int n,int step){
		StringBuilder sb = new StringBuilder(n);		
		for (int i = 0; i < n; i++) {
			if (i!=0&&i%step==0) {
				sb.append(i);
				i+=(int)Math.log10(i);
			}
			else sb.append(' ');
		}
		return sb.substring(0, n);
	}
	
	public AlnPanelItself(Alignment a,int upstream) {
	
	JPanel protnames = new JPanel();
	protnames.setLayout(new BoxLayout(protnames, BoxLayout.Y_AXIS));
	for (Iterator<String> iterator = a.names.iterator(); iterator.hasNext();) {
		String type = iterator.next();
		protnames.add(new JLabel(type+" "+ScoresMapper.genomeNames.get(type.split("\\|")[3])));			
	}
	
	JTextArea text = new JTextArea();
	text.scrollRectToVisible(getBounds());
	text.setFont(new Font("monospaced", Font.PLAIN, 18));

	text.append(makeBenchmarkNumbers(a.length, 10));
	text.append("\n");

	int lc=1;
	for (Iterator<String> iterator = a.lines.iterator(); iterator.hasNext();) {
		String type = iterator.next();
		int ng=0;
		int k=0;
		while(ng<upstream){
			if (type.charAt(k)!='-') ng++;
			k++;
		}
		text.append(type.substring(0,k).toLowerCase());
		text.append(type.substring(k,a.length));
		text.append("\n");
		int start=0;
		int stop=0;
		String codon="";
		for (int i = 0; i < type.length(); i++) {
			if (type.charAt(i)!='-') {
				codon+=type.charAt(i);
				if (codon.length()==3){
					if (codon.toUpperCase().equals("ATG")||codon.toUpperCase().equals("GTG")||codon.toUpperCase().equals("TTG")){
						try {
							text.getHighlighter().addHighlight(lc*(a.length+1)+start, lc*(a.length+1)+stop, new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));

						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
					else if (codon.toUpperCase().equals("TGA")||codon.toUpperCase().equals("TAA")||codon.toUpperCase().equals("TAG")){
						try {
							text.getHighlighter().addHighlight(lc*(a.length+1)+start, lc*(a.length+1)+stop, new DefaultHighlighter.DefaultHighlightPainter(Color.gray));

						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
					codon="";
					start=i+1;
					stop=i+1;
				}
			}
			stop++;				
		}
		lc++;
	}
	text.append(a.consensus);

		
		JScrollPane scrollPane = new JScrollPane(text);
		   		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		scrollPane.setPreferredSize(new Dimension(1300, 500));
		
		this.add(protnames, BorderLayout.WEST);
		this.add(scrollPane, BorderLayout.CENTER);
	}
}
