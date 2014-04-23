package aln;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Alignment {
	
	public ArrayList<String> names;
	public ArrayList<String> lines;
	public int length;
	String alnName;
	
	public String consensus;
	
	public Alignment(ArrayList<String> names, ArrayList<String> lines) throws Exception{
		this.lines=lines;
		this.names=names;
		//check name length consistency
		if (names.size()!=lines.size()) throw new Exception("Alignment: unequal names and lines length");
		//checl lines length consistency
		int i=0;
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			int l = string.length();
			if (i==0) length = l;
			else {
				if (l!=length )  throw new Exception("Alignment: unequal length in lines");
			}
		}
		//make consensus
		char current=' ';
		int k;
		consensus="";
		for (int j = 0; j < length; j++) {
			i=0;
			k=0;
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				char c = string.charAt(j);
				if (i==0) current=c;
				else {
					if (c!=current) break;
				}
				k++;
				i++;
			}
			if (k==lines.size()) consensus+="*";
			else consensus+=" ";
		}
	}
	
	public static Alignment readAlignmentFromFile(String f){
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String l = br.readLine();
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> lines = new ArrayList<String>();
			String s = "";
			int j=0;
			while(l!=null){
				if (l.startsWith(">")) {
					names.add(l);
					if (j!=0) lines.add(s);
					s="";
				}
				else{
					s+=l;
				}
				l=br.readLine();
				j++;
			}
			lines.add(s);
			br.close();
			Alignment a = new Alignment(names, lines);
			a.setName(f.substring(f.lastIndexOf("/")+1));
			return a;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public String getName(){
		return alnName;
	}
	
	public void setName(String f){
		this.alnName=f;
	}
	
}
