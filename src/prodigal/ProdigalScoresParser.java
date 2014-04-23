package prodigal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ProdigalScoresParser {
	
	public static ProdigalRecord getProdigalfromFasta(String f,String prot_name) throws IOException{
		ProdigalRecord pr=null;
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String l = br.readLine();
		while(l!=null){
			if (l.startsWith(prot_name)){
				StringTokenizer st = new StringTokenizer(l,"#");
				st.nextToken();
				int beg = Integer.parseInt(st.nextToken().trim());
				int end = Integer.parseInt(st.nextToken().trim());
				int strand = Integer.parseInt(st.nextToken().trim());		
				String codon_line = st.nextToken().split(";")[2];
				String codon = codon_line.substring(codon_line.indexOf('=')+1);
				pr = new ProdigalRecord(beg, end, strand, 0,codon);
				break;
			}
			l=br.readLine();
		}
		br.close();
		
		return pr;
	}
	
	public static ProdigalRecord lookForProdigalfromFasta(String f,int MOstrand,int MOstrat,int MOend) throws IOException{
		ProdigalRecord pr=null;
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String l = br.readLine();
		while(l!=null){
			if (l.startsWith(">")){
				StringTokenizer st = new StringTokenizer(l,"#");
				st.nextToken();
				int beg = Integer.parseInt(st.nextToken().trim());
				int end = Integer.parseInt(st.nextToken().trim());
				int strand = Integer.parseInt(st.nextToken().trim());
				if ((strand==1&&MOstrand==1&&end==MOend)||(strand==-1&&MOstrand==-1&&beg==MOstrat)){
					String codon_line = st.nextToken().split(";")[2];
					String codon = codon_line.substring(codon_line.indexOf('=')+1);
					pr = new ProdigalRecord(beg, end, strand, 0,codon);
					break;
				}				
			}
			l=br.readLine();
		}
		br.close();
		
		return pr;
	}
	
	public static Hashtable<Integer,ProdigalRecord>[] readScoresFile(String f) throws IOException{
		
		Hashtable<Integer,ProdigalRecord> h1 = new Hashtable<Integer, ProdigalRecord>();
		Hashtable<Integer,ProdigalRecord> h_menos_1 = new Hashtable<Integer, ProdigalRecord>();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String l = br.readLine();
		int count=0;
		while(l!=null){
			if (!(l.trim().equals("")||l.startsWith("#")||l.startsWith("Beg"))){
				count++;
				StringTokenizer st = new StringTokenizer(l,"\t");
				int beg = Integer.parseInt(st.nextToken());
				int end = Integer.parseInt(st.nextToken());
				int strand = (st.nextToken().equals("+")?1:-1);
				double total = Double.parseDouble(st.nextToken());
				st.nextToken();
				st.nextToken();
				String codon = st.nextToken();
				ProdigalRecord pr = new ProdigalRecord(beg, end, strand, total, codon);
				if (strand==1) h1.put(beg, pr);
				else h_menos_1.put(end, pr);
			}
			l=br.readLine();
		}
		br.close();
		
//		System.out.println(h_menos_1.size());
//		System.out.println(h1.size());
//		System.out.println(count);
		
		Hashtable<Integer,ProdigalRecord>[] h = new Hashtable[2]; 
		h[0]=h_menos_1;
		h[1]=h1;
		return h;
	}
	
	public static void main(String[] args) throws IOException {
		String f = "/home/koshka/work/GENE_START/prodigal_res/ecoliDH10B.fasta.scores";
		readScoresFile(f);
	}
	
}













