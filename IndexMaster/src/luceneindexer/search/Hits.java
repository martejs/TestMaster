package luceneindexer.search;

import java.util.HashMap;
import java.util.TreeMap;


public class Hits {
	
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	private int occurrences = 0;
	
	public void setTerms(String term){
		if(!this.terms.containsKey(term)){
			this.occurrences = 1;
			terms.put(term, this.occurrences);
		}
		else{
			terms.put(term, terms.get(term) + 1);
		}
	}
	
	public HashMap<String, Integer> getTerms(){
		
		return this.terms;
	}

}
