package thmm;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import optimization.mcmc.MCMCParameters;

import model.StateModel;
import model.StateModelMatrix;

import solver.FBSolverLog;
import solver.MCMCFBSolver;
import solver.resultObjects.Event;
import tree.NewickString;
import tree.Tree;
import tree.Tree.proteinTreeElement;
import tree.reader.NewickStringReader;
import aln.Alignment;

public class Launch {
	
	public static double[][] getMatrix(int size,double rate_imit){
		double[][] res = new double[size][size];
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res.length; j++) {
				if (i==j) res[i][j]=0;
				else res[i][j]= rate_imit;
			}
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		String f = "/home/koshka/work/GENE_START/cases_ecoliW3110_ecoliDH10B_ecoliO157I_shigella_salmonella/less/muscle/case1.fasta.muscle";
		Alignment a = Alignment.readAlignmentFromFile(f);
		
		ArrayList<String> n = a.names;
		for (Iterator iterator = n.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}		
				
		Tree t = new Tree(NewickStringReader.readFromFile("/home/koshka/work/GENE_START/thmm_try/5genomes.nwk")); 
			
		Hashtable<String,double[]> h = new Hashtable<String, double[]>();
		String[] name =ScoresMapper.getMapping(a, h);
//		String[] name = {"0","1","2"};
		StateModel m = new StateModelMatrix(null,name,null) ;
		
	/*	Hashtable<String,double[]> h = new Hashtable<String, double[]>();
		h.put("salm", new double[]{0.9,0.06,0.04});
		h.put("shigella", new double[]{0.9,0.05,0.05});
		h.put("ecoliO157I", new double[]{0.9,0.03,0.07});
		h.put("ecoliW3110", new double[]{0.06,0.04,0.9});
		h.put("ecoliDH10B", new double[]{0.02,0.03,0.95});		
		*/
		
		System.out.println(h.get("salmonella").length+" "+name.length);
		
		MCMCParameters p = new MCMCParameters(1, 0,1);
//		p.setRateLimits(new double[][]{{0,2,2},{2,0,2},{2,2,0}});
		p.setRateLimits(getMatrix(name.length, 1));
		
			
		MCMCFBSolver mf = new MCMCFBSolver(t, m, p, h);
		
		FBSolverLog res = mf.solveSample(3);
		
		System.out.println("Events number:"+res.getEventsNumber());	
		ArrayList<Event>[][] e = res.getEvents();
		for (int i = 0; i < e.length; i++) {
			for (int j = 0; j < e[i].length; j++) {
				ArrayList<Event> aa = e[i][j];
				if (aa==null) continue;
				for (Iterator iterator = aa.iterator(); iterator.hasNext();) {
					Event type = (Event) iterator.next();
					System.out.println(type.toString());
				}
			}
		}
		
		for (Iterator iterator = t.v.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			proteinTreeElement pte = t.v.get(type);
			System.out.println(pte.name);
			double[] fb = pte.fbi.FB;
			double[] priors = pte.fbi.priors;
			for (int i = 0; i < fb.length; i++) {
				System.out.println(fb[i]);
//				System.out.println(priors[i]);
			}
		}
	}
}
