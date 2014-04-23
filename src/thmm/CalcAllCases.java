package thmm;

import graph2.CasesPanel;

import java.io.File;
import java.util.Arrays;

import solver.FBSolverLog;

public class CalcAllCases {
	
	public static final String dir = "cases_ecoliW3110_ecoliDH10B_ecoliO157I_shigella_salmonella/less/muscle/"; 
	
	public static int[] doAll(double alpha,double lambda) throws Exception{
		File f = new File(CasesPanel.MOG_alnDir);
		File[] ff = f.listFiles();
		Arrays.sort(ff);
		int count = 0;
		int countZeroCases = 0;
//		System.out.println("number of cases:"+ff.length);
		for (int i = 0; i < ff.length; i++) {
			String alnName = ff[i].getAbsolutePath();
			final String treeFile =CasesPanel.MOG_treeDir+alnName.substring(alnName.lastIndexOf("/"))+".nwk";
//			FBSolverLog res = Launch.thmm(ff[i].getAbsolutePath(), alpha, lambda);
			FBSolverLog res = LaunchMO.thmm(ff[i].getAbsolutePath(), treeFile ,alpha, lambda);
			int e = res.getEventsNumber();
//			System.out.println(i+" "+ff[i]+" "+e);
			count+=e;
			if (e==0) {
				System.out.println(ff[i]);
				countZeroCases++;
			}
		}
		return (new int[]{count,countZeroCases});
	}
	
	public static void testAlphaLambdaLog(double startA,double byA,int nA,double startL, double byL, int nL) throws Exception{
		int resEvents[][] = new int[nA][nL];
		int resZeroCases[][] = new int[nA][nL];
		double[] alphas = new double[nA];
		double[] lambdas = new double[nL];
		for (int i = 0; i < nA; i++) {
			double alpha=startA/Math.pow(byA, i);
			alphas[i]=alpha;
			for (int j = 0; j <nL; j++) {
				double lambda =startL/Math.pow(byL, j);
				if (i==0) lambdas[j]=lambda;
				int[] r = doAll(alpha,lambda);
				resEvents[i][j] = r[0];
				resZeroCases[i][j] = r[1];
			}
		}	
		
		System.out.println("========events=========");
		System.out.println("\t");
		for (int j = 0; j < lambdas.length; j++) {
			System.out.print("\t"+lambdas[j]);
		}
		System.out.println();
		for (int i = 0; i < alphas.length; i++) {
			System.out.print(alphas[i]);
			for (int j = 0; j < lambdas.length; j++) {
				System.out.print("\t"+resEvents[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("========zero cases=========");
		System.out.println("\t");
		for (int j = 0; j < lambdas.length; j++) {
			System.out.print("\t"+lambdas[j]);
		}
		for (int i = 0; i < alphas.length; i++) {
			System.out.print(alphas[i]);
			for (int j = 0; j < lambdas.length; j++) {
				System.out.print("\t"+resZeroCases[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws Exception {
		double alpha=0.01;
		double lambda = 1;
		int[] r = doAll(alpha, lambda);
		System.out.println(r[0]+" "+r[1]);
//		testAlphaLambdaLog(1, 3, 10, 10, 2, 10);
	}
}
