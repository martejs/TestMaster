package luceneindexer.search;

import java.util.HashMap;
import java.util.TreeMap;


public class Hits {
	
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	private int occurrences = 0;
	
	public void setTerms(String term){
		if(!this.terms.containsKey(term)){
			this.occurrences = 1;
			System.out.println("f¿rste gang: " + term);
			System.out.println(this.occurrences);
			terms.put(term, this.occurrences);
		}
		else{
			System.out.println("igjen:" + term);
			this.occurrences++;
			System.out.println(this.occurrences);
			terms.put(term, this.occurrences);
		}
	}
	
	public HashMap<String, Integer> getTerms(){
		
		return this.terms;
	}

}
