package luceneindexer.search;

import java.util.HashMap;


public class Hits {
	
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	private int occurrences = 1;
	
	public void setTerms(String term){
		if(this.terms.containsKey(term)){
			terms.put(term, occurrences++);
		}
		else{
			terms.put(term, occurrences);
		}
	}
	
	public HashMap<String, Integer> getTerms(){
		
		return this.terms;
	}

}
