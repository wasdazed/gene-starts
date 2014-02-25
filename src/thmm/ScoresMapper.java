package thmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import prodigal.ProdigalRecord;
import prodigal.ProdigalScoresParser;

import aln.Alignment;

public class ScoresMapper {
	
	public static String prod_res_dir="/home/koshka/work/GENE_START/prodigal_res";
	public static Hashtable<String,String> genomeNames;
	static
	{
		genomeNames = new Hashtable<String,String>();
		genomeNames.put("NC_011083.1", "salmonella");
		genomeNames.put("NC_010473.1", "ecoliDH10B");
		genomeNames.put("NC_007779.1", "ecoliW3110");
		genomeNames.put("NC_007606.1", "shigella");
		genomeNames.put("NC_002695.1", "ecoliO157I");
	}
	
	public static Hashtable<Integer,ProdigalRecord>[] getScores(String genome) throws IOException{
		String name = genomeNames.get(genome);
//		System.out.println(name);
		String f = prod_res_dir+"/"+name+".fasta.scores";
		Hashtable<Integer,ProdigalRecord>[]  res = ProdigalScoresParser.readScoresFile(f);
		return res;
	}
	
	public static ProdigalRecord getAlnStart(String genome,String prot_name) throws IOException{
		String name = genomeNames.get(genome);
//		System.out.println(name);
		String f = prod_res_dir+"/"+name+".fasta.aa";
		ProdigalRecord pr = ProdigalScoresParser.getProdigalfromFasta(f, prot_name);
		
//		System.out.println(pr.toString());
		return pr;
	}
	
	public static String[] getMapping(Alignment a,Hashtable<String,double[]> res) throws IOException{
		
		int[] starts = new int[a.names.size()];
		int[] strands = new int[a.names.size()];
		Hashtable<Integer,ProdigalRecord>[] h = new Hashtable[a.names.size()];
		int curr_strand = -2;
		for (int j = 0; j < a.names.size(); j++) {
			String string = a.names.get(j);
		
			String g_name = string.split("\\|")[3];
//			System.out.println(g_name);
//			System.out.println(string);
			
			ProdigalRecord pr = getAlnStart(g_name, string);
			
			int strand = pr.getStrand();
			if (j!=0&&strand!=curr_strand) System.out.println("strands doesn't match");
			curr_strand=strand;	
			
			strands[j]=curr_strand;
			System.out.println(j+" "+curr_strand);
			if (curr_strand>0) starts[j] = pr.getBeg()-300;
			else starts[j] = pr.getEnd()+300;
			
			h[j]=getScores(g_name)[curr_strand>0?1:0];				
		}
		
			System.out.println("================aln===analysis============");	
		String s;
		double[][] scores_aln =new double[a.names.size()][a.length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.names.size(); j++) {
				s = a.lines.get(j);
				if (s.charAt(i)!='-') {
					if (h[j].containsKey(starts[j])){
						ProdigalRecord pr = h[j].get(starts[j]);
						scores_aln[j][i]=pr.getTotalScore();
//						System.out.println(i+" "+j+" "+starts[j]+" "+s.substring(i,i+3)+" "+pr.getCodon());
						if (!s.substring(i,i+3).equals(pr.getCodon())) System.out.println(j+" "+s.substring(i,i+3)+" "+pr.getCodon());
					}
					if (strands[j]>0) starts[j]++;
					else starts[j]--;
				}
			}
			
		}
		int count=0;
		System.out.println("Alignment length:"+scores_aln[0].length);
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
			}
		}
		System.out.println("Not empty cols:"+count);
		
		int n = 0;
		int m = 12;
		ArrayList<Integer> a_state_cols2 = new ArrayList<Integer>();
		for (int i = n; i < m; i++) {
			a_state_cols2.add(a_state_cols.get(i));
		}
		
		a_state_cols=a_state_cols2;
		System.out.println("State cols size:"+a_state_cols.size());
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
			String g = genomeNames.get(a.names.get(i).split("\\|")[3]);
			res.put(g, sr);
		}
		
		return names;
	}
	
		
	public static void main(String[] args) throws IOException {
		String f = "/home/koshka/work/GENE_START/cases_ecoliW3110_ecoliDH10B_ecoliO157I_shigella_salmonella/less/muscle/case2.fasta.muscle";
		Alignment a = Alignment.readAlignmentFromFile(f);			
	}
}
