package thmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import model.StateModel;
import model.StateModelMatrix;
import solver.FBSolverLog;
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
				if (i==j) res[i][j]=-rate_imit*(size-1);
				else res[i][j]= rate_imit;
			}
		}
		return res;
	}
	
	public static double[][] getDistanceMatrix(String[] names,double alpha,double lambda){
		double[][] res = new double[names.length][names.length];
		for (int i = 0; i < res.length; i++) {
			double sum = 0;
			for (int j = 0; j < res.length; j++) {
				if (i==j) continue;
				else {
					res[i][j]= lambda*Math.exp(-alpha*Math.abs((Integer.parseInt(names[i])-Integer.parseInt(names[j]))));
					sum+=res[i][j];
				}
			}
			res[i][i]=-sum;
		}
		return res;
	}
	
//	public static FBSolverLog thmm(String f) throws Exception{
	public static String thmmString(String f,double alpha,double lambda) throws Exception{
		
		Alignment a = Alignment.readAlignmentFromFile(f);
		
		ArrayList<String> n = a.names;
		for (Iterator iterator = n.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
//			System.out.println(string);
		}		
		
		StringBuilder sb = new StringBuilder();
		
//		NewickString ns = NewickStringReader.readFromFile("/home/koshka/work/GENE_START/thmm_try/5genomes.nwk");
		NewickString ns = NewickStringReader.readFromFile("5genomes.nwk");
		Tree t = new Tree(ns); 
		
		sb.append("=====================TREE=========================\n");
		sb.append(ns.getNewickString()+"\n");
			
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
		
		sb.append("=================STATES (ALN POSITIONS)=================\n");
		sb.append("\t");
		for (int i = 0; i < name.length; i++) {
			sb.append("\t"+name[i]);
		}
		sb.append("\n");
		
		sb.append("=================SCORE HASHTABLE h=================\n");
		for (Iterator iterator = h.keySet().iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			double [] e = h.get(string);
			sb.append(string);
			for (int i = 0; i < e.length; i++) {
				sb.append("\t"+e[i]);
			}
			sb.append("\n");
		}
		
				
//		MCMCParameters p = new MCMCParameters(1, 0,1);
//		p.setRateLimits(new double[][]{{0,2,2},{2,0,2},{2,2,0}});
//		p.setRateLimits(getMatrix(name.length, 1));
		
			
//		MCMCFBSolver mf = new MCMCFBSolver(t, m, p, h);
	
//		double[][] q =getMatrix(name.length, 1);
//		double alpha = (double)1/3;
//		double alpha = 0.01;
//		double lambda = 2;
		double[][] q =getDistanceMatrix(name, alpha,lambda);
		
		sb.append("ALPHA:"+alpha+"\n");
		sb.append("LAMBDA:"+lambda+"\n");
		
		sb.append("=================RATE MATRIX q=================\n");
		for (int i = 0; i < q.length; i++) {
			for (int j = 0; j < q.length; j++) {
				sb.append("\t"+q[i][j]);
			}
			sb.append("\n");
		}
		m.setParameters(q);
		
		FBSolverLog res = new FBSolverLog(t, m, h);
		
		res.solveSample();
		sb.append("\n");
		sb.append("===============RESULTS======================\n");
		sb.append("tree P: "+res.getTreeP()+"\n");
		
		sb.append("Events number:"+res.getEventsNumber()+"\n");	
		ArrayList<Event>[][] e = res.getEvents();
		for (int i = 0; i < e.length; i++) {
			for (int j = 0; j < e[i].length; j++) {
				ArrayList<Event> aa = e[i][j];
				if (aa==null) continue;
				for (Iterator iterator = aa.iterator(); iterator.hasNext();) {
					Event type = (Event) iterator.next();
					sb.append(type.toString()+"\n");
				}
			}
		}
		sb.append("===============PROBABILITIES AT NODES======================\n");
		for (Iterator iterator = t.v.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			proteinTreeElement pte = t.v.get(type);
			sb.append(pte.name+"\n");
			double[] fb = pte.fbi.FB;
			double[] priors = pte.fbi.priors;
			for (int i = 0; i < fb.length; i++) {
				sb.append(fb[i]+"\n");
				//		System.out.println(priors[i]);
			}
		}

				
		return sb.toString();
		
	}
	
public static FBSolverLog thmm(String f,double alpha,double lambda) throws Exception{
		
		Alignment a = Alignment.readAlignmentFromFile(f);
		
		ArrayList<String> n = a.names;
/*		for (Iterator iterator = n.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
//			System.out.println(string);
		}*/		
		
//		NewickString ns = NewickStringReader.readFromFile("/home/koshka/work/GENE_START/thmm_try/5genomes.nwk");
		NewickString ns = NewickStringReader.readFromFile("5genomes.nwk");
		Tree t = new Tree(ns); 
		
			
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
		
						
//		MCMCParameters p = new MCMCParameters(1, 0,1);
//		p.setRateLimits(new double[][]{{0,2,2},{2,0,2},{2,2,0}});
//		p.setRateLimits(getMatrix(name.length, 1));
		
			
//		MCMCFBSolver mf = new MCMCFBSolver(t, m, p, h);
	
//		double[][] q =getMatrix(name.length, 1);
//		double alpha = (double)1/3;
//		double alpha = 0.01;
//		double lambda = 2;
		double[][] q =getDistanceMatrix(name, alpha,lambda);
		
		m.setParameters(q);
		
		FBSolverLog res = new FBSolverLog(t, m, h);
		
		res.solveSample();
					
		return res;
		
	}


public static void main(String[] args) throws Exception {
	String f = "/home/koshka/work/GENE_START/cases_ecoliW3110_ecoliDH10B_ecoliO157I_shigella_salmonella/less/muscle/case1.fasta.muscle";
/*	FBSolverLog res = thmm(f);
	
	System.out.println("tree P: "+res.getTreeP());
	
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
	
	Tree t = res.getTree();	
	for (Iterator iterator = t.v.keySet().iterator(); iterator.hasNext();) {
		String type = (String) iterator.next();
		proteinTreeElement pte = t.v.get(type);
		System.out.println(pte.name);
		double[] fb = pte.fbi.FB;
		double[] priors = pte.fbi.priors;
		for (int i = 0; i < fb.length; i++) {
			System.out.println(fb[i]);
			//		System.out.println(priors[i]);
		}
	}*/
}

}







