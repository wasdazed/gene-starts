package graph2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import prodigal.ProdigalRecord;
import prodigal.ProdigalScoresParser;
import aln.Alignment;

public class AlnPanelItself extends JPanel{
	
	public int[] strands;
	public int[] starts;

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

	public AlnPanelItself(Alignment a,String prodigal_res_dir,Hashtable<Integer,ProdigalRecord>[] scoresHash) throws IOException {
		
		starts = new int[a.names.size()];
		strands = new int[a.names.size()];
		
		this.setLayout(new BorderLayout(50,5));
		
		JPanel protnames = new JPanel();
		protnames.setLayout(new GridLayout(a.names.size(),3,5,1));
		
//		protnames.setLayout(new BoxLayout(protnames, BoxLayout.Y_AXIS));
		/*for (Iterator<String> iterator = a.names.iterator(); iterator.hasNext();) {
		String type = iterator.next();
		protnames.add(new JLabel(type));			
	}*/

		JTextArea text = new JTextArea(a.names.size()+3,a.length);
//		JTextArea text = new JTextArea();
//		text.scrollRectToVisible(getBounds());
		text.setFont(new Font("monospaced", Font.PLAIN, 18));

		text.append(makeBenchmarkNumbers(a.length, 10));
		text.append("\n");

		int lc=1;
		for (int in = 0; in < a.names.size(); in++) {
			String type = a.lines.get(in);
			String name = a.names.get(in);

			String locus[] = name.substring(1).split("\\|");
			int strand = Integer.parseInt(locus[6]);
			
			strands[in]=strand;
			
			String f = prodigal_res_dir+"/"+locus[0]+"_contig"+locus[1]+".fasta";		
			ProdigalRecord pr = ProdigalScoresParser.lookForProdigalfromFasta(f+".aa", strand, Integer.parseInt(locus[7]), Integer.parseInt(locus[8]));

			scoresHash[in]=ProdigalScoresParser.readScoresFile(f+".scores")[strand>0?1:0];				
			
			int upstream = 0;
			int MO_upstream=-1;
			
			String gName = TestFrame.gNames.get(Integer.parseInt(locus[0]));
			
			protnames.add(new JLabel(gName));
			protnames.add(new JLabel(name));
			
			if (strand==1) {
				if (Integer.parseInt(locus[7])!=pr.getBeg()) {
					protnames.add(new JLabel("prodigal:"+pr.getBeg()));
					MO_upstream = Integer.parseInt(locus[7])-Integer.parseInt(locus[9]);
				}
				else protnames.add(new JLabel("coincide"));
				upstream = pr.getBeg() - Integer.parseInt(locus[9]);
				starts[in]= Integer.parseInt(locus[9]);
			}
			else if (strand==-1) {
				if (Integer.parseInt(locus[8])!=pr.getEnd()) {
					protnames.add(new JLabel("prodigal:"+pr.getEnd()));
					MO_upstream = Integer.parseInt(locus[10])-Integer.parseInt(locus[8])-1;
				}
				else protnames.add(new JLabel("coincide"));
				upstream = Integer.parseInt(locus[10])-pr.getEnd()-1;
				starts[in]= Integer.parseInt(locus[10])-1;
			}
			
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
			ng=0;
			for (int i = 0; i < type.length(); i++) {
				if (type.charAt(i)!='-') {
					ng++;
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
				if (ng==MO_upstream&&MO_upstream>0) {
					try {
						text.getHighlighter().addHighlight(lc*(a.length+1)+i+1, lc*(a.length+1)+i+2, new DefaultHighlighter.DefaultHighlightPainter(Color.pink));

					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
			lc++;
		}
		text.append(a.consensus);


		JScrollPane scrollPane = new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

//		scrollPane.setPreferredSize(new Dimension(800, 500));

		this.add(protnames, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
	}
}
