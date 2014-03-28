package luceneindexer.search;

public class Term2 {
	
	private String term1 ="";
	private String term2 = "";
	private float MI;
	private float chi;
	private float tfIdf;
	
	
	public Term2(String term1, String term2, int totDoc){
		setTerm1(term1);
		setTerm2(term2);
		setTfIdf(totDoc);
		
	}
	
	public float getTfIdf() {
		return tfIdf;
	}
	public void setTfIdf(float tfIdf) {
		this.tfIdf = tfIdf;
	}
	public float getChi() {
		return chi;
	}
	public void setChi(float chi) {
		this.chi = chi;
	}
	public float getMI() {
		return MI;
	}
	public void setMI(float mI) {
		MI = mI;
	}
	public String getTerm2() {
		return term2;
	}
	public void setTerm2(String term2) {
		this.term2 = term2;
	}
	public String getTerm1() {
		return term1;
	}
	public void setTerm1(String term1) {
		this.term1 = term1;
	}
	

}