package thmm;

import graph2.CasesPanel;
import graph2.TestFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JLabel;

import prodigal.ProdigalRecord;
import prodigal.ProdigalScoresParser;
import aln.Alignment;

public class ScoresMapperMO {
	
	public static String prod_res_dir="/home/koshka/work/GENE_START/V2_MO/prodigal_res";
//	public static String prod_res_dir="prodigal_res";
	
	private static void clean(double[][] s,int j,int i){
		for (int k = 0; k < i; k++) {
			s[j][k]=0;
		}
	}
	
	public static String[] getMapping(Alignment a,Hashtable<String,double[]> res,Hashtable<Integer,ProdigalRecord>[] h, int[] starts,int[] strands) throws IOException{
		
		
		int[] starts_fixed = new int[a.names.size()];
		
		
		starts_fixed=starts.clone();
		
//		System.out.println("================aln===analysis============");	
		String s;
		double[][] scores_aln =new double[a.names.size()][a.length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.names.size(); j++) {
				s = a.lines.get(j);
				if (s.charAt(i)!='-') {
					if (i>a.length-90) continue;
					if (Math.abs(starts[j]-starts_fixed[j])%3==0){
					String codon = s.substring(i,i+3);
					if (codon.toUpperCase().equals("TGA")||codon.toUpperCase().equals("TAA")||codon.toUpperCase().equals("TAG")){
//						System.out.println(j+" "+i+" "+codon);
						clean(scores_aln,j,i);
					}
					}
					if (h[j].containsKey(starts[j])&&Math.abs(starts[j]-starts_fixed[j])%3==0){
						ProdigalRecord pr = h[j].get(starts[j]);
						scores_aln[j][i]=pr.getTotalScore();
//						System.out.println(i+" "+j+" "+a.names.get(j)+" "+starts[j]+" "+s.substring(i,i+3)+" "+pr.getCodon());
//						if (!s.substring(i,i+3).equals(pr.getCodon())) System.out.println(j+" "+s.substring(i,i+3)+" "+pr.getCodon());
					}
					if (strands[j]>0) starts[j]++;
					else starts[j]--;
				}
			}
			
		}
		int count=0;
//		System.out.println("Alignment length:"+scores_aln[0].length);
		ArrayList<Integer> a_state_cols = new ArrayList<Integer>();
		for (int i = 0; i < scores_aln[0].length; i++) {
			boolean empty_col=true;
			for (int j = 0; j < scores_aln.length; j++) {
				if(scores_aln[j][i]!=0) {
					empty_col=false;
					break;
				}
			}
			if (!empty_col) {
				count++;
				a_state_cols.add(i);
//				System.out.println(i);
			}
		}
//		System.out.println("Not empty cols:"+count);
		
	/*	int n = 0;
		int m = 12;
		ArrayList<Integer> a_state_cols2 = new ArrayList<Integer>();
		for (int i = n; i < m; i++) {
			a_state_cols2.add(a_state_cols.get(i));
		}
		
		a_state_cols=a_state_cols2;*/
//		System.out.println("State cols size:"+a_state_cols.size());
		String[] names = new String[a_state_cols.size()];
		for (int i = 0; i < scores_aln.length; i++) {
			double[] sr = new double[a_state_cols.size()];
			int k = 0; 
			for (Iterator<Integer> iterator = a_state_cols.iterator(); iterator.hasNext();) {
				Integer integer = iterator.next();
				sr[k]=scores_aln[i][integer];
				names[k]=""+integer;
				k++;
				
			}
//			String g = genomeNames.get(a.names.get(i).split("\\|")[3]);
//			System.out.println(a.names.get(i).substring(1));
//			for (int j = 0; j < sr.length; j++) {
//				System.out.println(sr[j]);
//			}
			res.put(a.names.get(i).substring(1), sr);
		}
		
		return names;
	}
	
	public static String[] getMapping2(Alignment a,Hashtable<String,double[]> res) throws IOException{

		int[] starts = new int[a.names.size()];
		int[] starts_fixed = new int[a.names.size()];		
		int[] strands = new int[a.names.size()];
		Hashtable<Integer,ProdigalRecord>[] h = new Hashtable[a.names.size()];
		int curr_strand = -2;

		for (int in = 0; in < a.names.size(); in++) {
			String type = a.lines.get(in);
			String name = a.names.get(in);

			String locus[] = name.substring(1).split("\\|");
			int strand = Integer.parseInt(locus[6]);

			strands[in]=strand;

			String f = CasesPanel.prodigalDir+"/"+locus[0]+"_contig"+locus[1]+".fasta";		
			ProdigalRecord pr = ProdigalScoresParser.lookForProdigalfromFasta(f+".aa", strand, Integer.parseInt(locus[7]), Integer.parseInt(locus[8]));

			h[in]=ProdigalScoresParser.readScoresFile(f+".scores")[strand>0?1:0];				

			int upstream = 0;
			int MO_upstream=-1;

			String gName = TestFrame.gNames.get(Integer.parseInt(locus[0]));

			if (strand==1) {
				if (Integer.parseInt(locus[7])!=pr.getBeg()) {
					MO_upstream = Integer.parseInt(locus[7])-Integer.parseInt(locus[9]);
				}
				upstream = pr.getBeg() - Integer.parseInt(locus[9]);
				starts[in]= Integer.parseInt(locus[9]);
			}
			else if (strand==-1) {
				if (Integer.parseInt(locus[8])!=pr.getEnd()) {
					MO_upstream = Integer.parseInt(locus[10])-Integer.parseInt(locus[8])-1;
				}
				upstream = Integer.parseInt(locus[10])-pr.getEnd()-1;
				starts[in]= Integer.parseInt(locus[10])-1;
			}
		}

			starts_fixed=starts.clone();

			//		System.out.println("================aln===analysis============");	
			String s;
			double[][] scores_aln =new double[a.names.size()][a.length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a.names.size(); j++) {
					s = a.lines.get(j);
					if (s.charAt(i)!='-') {
						if (i>a.length-90) continue;
						if (Math.abs(starts[j]-starts_fixed[j])%3==0){
							String codon = s.substring(i,i+3);
							if (codon.toUpperCase().equals("TGA")||codon.toUpperCase().equals("TAA")||codon.toUpperCase().equals("TAG")){
								//						System.out.println(j+" "+i+" "+codon);
								clean(scores_aln,j,i);
							}
						}
						if (h[j].containsKey(starts[j])&&Math.abs(starts[j]-starts_fixed[j])%3==0){
							ProdigalRecord prs = h[j].get(starts[j]);
							scores_aln[j][i]=prs.getTotalScore();
							//						System.out.println(i+" "+j+" "+a.names.get(j)+" "+starts[j]+" "+s.substring(i,i+3)+" "+pr.getCodon());
							//						if (!s.substring(i,i+3).equals(pr.getCodon())) System.out.println(j+" "+s.substring(i,i+3)+" "+pr.getCodon());
						}
						if (strands[j]>0) starts[j]++;
						else starts[j]--;
					}
				}

			}
			int count=0;
			//		System.out.println("Alignment length:"+scores_aln[0].length);
			ArrayList<Integer> a_state_cols = new ArrayList<Integer>();
			for (int i = 0; i < scores_aln[0].length; i++) {
				boolean empty_col=true;
				for (int j = 0; j < scores_aln.length; j++) {
					if(scores_aln[j][i]!=0) {
						empty_col=false;
						break;
					}
				}
				if (!empty_col) {
					count++;
					a_state_cols.add(i);
					//				System.out.println(i);
				}
			}
			//		System.out.println("Not empty cols:"+count);

			/*	int n = 0;
		int m = 12;
		ArrayList<Integer> a_state_cols2 = new ArrayList<Integer>();
		for (int i = n; i < m; i++) {
			a_state_cols2.add(a_state_cols.get(i));
		}

		a_state_cols=a_state_cols2;*/
			//		System.out.println("State cols size:"+a_state_cols.size());
			String[] names = new String[a_state_cols.size()];
			for (int i = 0; i < scores_aln.length; i++) {
				double[] sr = new double[a_state_cols.size()];
				int k = 0; 
				for (Iterator<Integer> iterator = a_state_cols.iterator(); iterator.hasNext();) {
					Integer integer = iterator.next();
					sr[k]=scores_aln[i][integer];
					names[k]=""+integer;
					k++;

				}
				res.put(a.names.get(i).substring(1), sr);
			}

			return names;
		
	}
		
	public static void main(String[] args) throws IOException {
	/*	String f = "/home/koshka/work/GENE_START/V2_MO/MO_COG_aln/COG859.fasta.aln";
		Alignment a = Alignment.readAlignmentFromFile(f);
		Hashtable<String, double[]> res = new Hashtable<>();
		String[] states = getMapping(a, res);*/
	}
}
