package prodigal;

public class ProdigalRecord {
	 
	int beg;
	int end;
	int strand;
	double totalScore;
	String codon;
	
	public ProdigalRecord(int beg, int end, int strand, double totalScore,String codon) {
		super();
		this.beg = beg;
		this.end = end;
		this.strand = strand;
		this.totalScore = totalScore;
		this.codon = codon;
	}

	@Override
	public String toString() {
		return "ProdigalRecord [beg=" + beg + ", end=" + end + ", strand="
				+ strand + ", totalScore=" + totalScore + ", codon=" + codon
				+ "]";
	}
	
	public int getBeg() {
		return beg;
	}
	public int getEnd() {
		return end;
	}
	public int getStrand() {
		return strand;
	}
	public double getTotalScore() {
		return totalScore;
	}
	public String getCodon() {
		return codon;
	}
}
