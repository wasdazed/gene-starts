package thmm;

import java.io.IOException;
import java.util.Iterator;

import tree.NewickString;
import tree.Tree;
import tree.Tree.proteinTreeElement;
import tree.reader.NewickStringReader;
import aln.Alignment;

public class Launch {
	
	public static void main(String[] args) throws IOException {
		String f = "/home/koshka/work/GENE_START/cases_ecoliW3110_ecoliDH10B_ecoliO157I_shigella_salmonella/less/muscle/case1.fasta.muscle";
		Alignment a = Alignment.readAlignmentFromFile(f);
				
		Tree t = new Tree(NewickStringReader.readFromFile("/home/koshka/work/GENE_START/thmm_try/5genomes.nwk")); 
		
		
	}
}
