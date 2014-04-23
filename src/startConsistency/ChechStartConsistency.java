package startConsistency;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import prodigal.ProdigalRecord;
import prodigal.ProdigalScoresParser;
import aln.Alignment;

public class ChechStartConsistency {
	
	public static String prod_res_dir="/home/koshka/work/GENE_START/V2_MO/prodigal_res";
	public static String aln_dir="/home/koshka/work/GENE_START/V2_MO/MO_MOG_aln_muscle";
	public static String aln_dir2="/home/koshka/work/GENE_START/V2_MO/MO_MOG_aln_selected";
	
	public static Hashtable<String,int[]> missingProdigalAA;
	public static Hashtable<String,int[]> prodigalOutOfScope;
	public static Hashtable<String,int[]> prodigalBothProblems;
	
	public static int checkFile(String alnFile) throws NumberFormatException, IOException{
		Alignment a = Alignment.readAlignmentFromFile(alnFile);
		
		int[] start_cols = new int[a.names.size()];
		int notFoundPr = 0;
		int outOfScope = 0;
		for (int j = 0; j < a.names.size(); j++) {
			String string = a.names.get(j);
		
//			System.out.println(string);
			String locus[] = string.substring(1).split("\\|");
			String f = prod_res_dir+"/"+locus[0]+"_contig"+locus[1]+".fasta";
			int strand = Integer.parseInt(locus[6]);		
			
			ProdigalRecord pr = ProdigalScoresParser.lookForProdigalfromFasta(f+".aa", strand, Integer.parseInt(locus[7]), Integer.parseInt(locus[8]));
			
			if (pr==null) {
//				System.out.println("Can't find coresponding prodigal result!");
//				System.out.println(string);
				notFoundPr++;
				continue;
			}
//			System.out.println(pr.toString());
			 
			String line = a.lines.get(j);
			
			int upstream_start = Integer.parseInt(locus[9]);
			if (strand==-1) upstream_start = Integer.parseInt(locus[10]);	
//			System.out.println(upstream_start);
			boolean gotit=false;
			if (strand==1){
				for (int i = 0; i < line.length(); i++) {
					if(line.charAt(i)!='-') upstream_start++;
					if (upstream_start==pr.getBeg()) {
						start_cols[j]=i+1;
						gotit=true;
//						System.out.println(line.substring(i+1,i+4));
					}
				}
			}else{
				for (int i = 0; i < line.length(); i++) {
					if(line.charAt(i)!='-') upstream_start--;
					if (upstream_start==pr.getEnd()) {
						start_cols[j]=i;
						gotit=true;
//						System.out.println(line.substring(i, i+3));
					}
				}
			}
			if (!gotit) {
//				System.out.println(string);
//				System.out.println("Didn't find start..");
				outOfScope++;
			}
			
		}
		if (notFoundPr>0){
			missingProdigalAA.put(alnFile, new int[]{notFoundPr,a.names.size()});
			return 22;
		}
		
		if (outOfScope>0){
			prodigalOutOfScope.put(alnFile, new int[]{outOfScope,a.names.size()});
			return 33;
		}
		
		if (notFoundPr>0&&outOfScope>0){
			prodigalBothProblems.put(alnFile, new int[]{notFoundPr,outOfScope,a.names.size()});
			return 44;
		}
		
		for (int i = 1; i < start_cols.length; i++) {
			if (start_cols[i]!=start_cols[0]) return 1;	
		}
		return 0;
	}
	
	public static void main(String[] args) throws IOException {
		
/*		prod_res_dir = args[2];
		aln_dir = args[0];
		aln_dir2 = args[1];
	*/	
		
		missingProdigalAA = new Hashtable<String,int[]>();
		prodigalOutOfScope = new Hashtable<String,int[]>();
		prodigalBothProblems = new Hashtable<String,int[]>();
		
		File aln = new File(aln_dir);
		File[] ff = aln.listFiles();
		int inconsistent = 0;
		ArrayList<File> res = new ArrayList<File>();
		try {
			for (int i = 0; i < ff.length; i++) {
				int status = checkFile(ff[i].getAbsolutePath());
				if (status==1) {
					res.add(ff[i]);
					inconsistent++;
				}
				else if (status==22) {
					
				}
				else if (status==33) {
					
				}
			}
			System.out.println("Alignments number: "+ff.length+"; of them inconsistent ones: "+inconsistent);			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("COGS/MOGS with missing Prodigal AA data: "+missingProdigalAA.size());
		for (Iterator<String> iterator = missingProdigalAA.keySet().iterator(); iterator.hasNext();) {
			String file =  iterator.next();
			int[] d = missingProdigalAA.get(file);
			System.out.println(file+" : "+d[0]+" records missing of "+d[1]);
			
		}
		
		System.out.println("COGS/MOGS with Prodigal out of scope data: "+prodigalOutOfScope.size());
		for (Iterator<String> iterator = prodigalOutOfScope.keySet().iterator(); iterator.hasNext();) {
			String file =  iterator.next();
			int[] d = prodigalOutOfScope.get(file);
			System.out.println(file+" : "+d[0]+" out of scope records of "+d[1]);
			
		}
		
		System.out.println("COGS/MOGS with really bad data: "+prodigalBothProblems.size());
		for (Iterator<String> iterator = prodigalBothProblems.keySet().iterator(); iterator.hasNext();) {
			String file =  iterator.next();
			int[] d = prodigalBothProblems.get(file);
			System.out.println(file+" : "+d[0]+" records missing; "+d[1]+" out of scope records; of "+d[2]);
			
		}
		File dir2 = new File(aln_dir2);
		if (!dir2.exists()) dir2.mkdir();
		else{
			File[] ff2 = dir2.listFiles();
			for (int i = 0; i < ff2.length; i++) {
				ff2[i].delete();
			}
		}
		for (Iterator<File> iterator = res.iterator(); iterator.hasNext();) {
			File string = iterator.next();
			String s2 = string.getAbsolutePath();
			File string2 = new File(aln_dir2+"/"+s2.substring(s2.lastIndexOf("/")));
//			System.out.println(string2);
			FileUtils.copyFile(string, string2);
		}
		
	}
	
}
