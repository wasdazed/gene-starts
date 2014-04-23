package graph2;

import graph.alnPanel;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;

import javax.swing.JFrame;

import util.Configuration;
import aln.Alignment;

public class TestFrame extends JFrame{
	
	private static final String genomeNamesResource = "/543genomesNames.csv";
	public static Hashtable<Integer, String> gNames;
	
	static{
		loadGenomeNames();
	}
	
	public TestFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		this.setLayout(new BorderLayout());
   		
   		tabs t = new tabs();
   		
   		t.addTab("MOG cases", CasesPanel.getMOGCasesPanel(t));
   		   		
		this.getContentPane().add(t,BorderLayout.CENTER);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);		
//		System.out.println(gNames.get(9));
	}

	public static void main(String[] args) {
		TestFrame tf = new TestFrame();
	}
	
	private static void loadGenomeNames(){
		gNames = new Hashtable<Integer,String>();
		try {
			Reader fr = new InputStreamReader(TestFrame.class.getResourceAsStream(genomeNamesResource));
			BufferedReader br = new BufferedReader(fr);
			String l = br.readLine();
			while(l!=null){
				String[] res = l.split(",");
				gNames.put(Integer.parseInt(res[0]), res[1]);
				l = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Can't load genome names");
			e.printStackTrace();			
		}
	}
}
	