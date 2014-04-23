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

public class LaunchMO {
	
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
	
	public static String thmmString(NewickString ns,String[] stateNames, Hashtable<String, double[]> h,double alpha,double lambda) throws Exception{						
		
		StringBuilder sb = new StringBuilder();
		
		Tree t = new Tree(ns,true);		
			
		StateModel m = new StateModelMatrix(null,stateNames,null) ;
		
		double[][] q = getDistanceMatrix(stateNames, alpha,lambda);
		
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
	
public static FBSolverLog thmm(String f,String treeFile,double alpha,double lambda) throws Exception{
		
		Alignment a = Alignment.readAlignmentFromFile(f);
		
		ArrayList<String> n = a.names;
		
		NewickString ns = NewickStringReader.readFromFile(treeFile);
		Tree t = new Tree(ns); 
		
			
		Hashtable<String,double[]> h = new Hashtable<String, double[]>();
		String[] name =ScoresMapperMO.getMapping2(a, h);

		StateModel m = new StateModelMatrix(null,name,null) ;
		
		double[][] q = getDistanceMatrix(name, alpha,lambda);
		
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







