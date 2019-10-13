
public class GeneIGCOUNT {
	
	String gene = "";
	double info  = 0;
	int cnt = 0;

	public GeneIGCOUNT(String GENE, double IG, int count) {
		
		gene = GENE;
		info = IG;
		cnt = count;
		
	}
	
	public String getGENE() {
		return gene;
	}
	
	public double getIG() {
		return info;
	}
	
	public int getCOUNT() {
		return cnt;
	}

}
