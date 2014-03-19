package luceneindexer.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;


public class Hits {
	
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	
	private int occurrences = 0;
	private float tf = 0;
	private float idf = 0;
	private float tfidf = 0; 
	private TermHits t = new TermHits();
	
	public void setTerms(String term, String resource){
		if(!this.terms.containsKey(term)){
			
//			System.out.println("ny term: " + term + " Ressurs " + resource);
			
			
			this.occurrences = 1;
			terms.put(term, this.occurrences);
		}
		else{
			terms.put(term, terms.get(term) + 1);
		}
		t.setDocumentFrequency(term, resource);
	}
	
	public HashMap<String, Integer> getTerms(){
		Iterator i = valueIterator(terms);
		
		for(terms.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			
			int size = this.terms.size();
			tfidf = getTfidf(value, size);
			
			System.out.println(key + ":" + value /*+ " Tf/idf: " + this.tfidf*/ + " "  + t.getResourceListLength(key));
			
			
		}
		return this.terms;
	}
	
	
	public float getTfidf(int value, int size){
		
		tf = (float) value / size;
		idf = (float) (Math.log(size/value)); 
		tfidf = (tf*idf);
		
		return tfidf;
	}
	
	Iterator valueIterator(HashMap<String, Integer> terms2) {
        Set set = new TreeSet(new Comparator<Map.Entry<String, Integer>>() {
        
        	
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return  o2.getValue().compareTo(o1.getValue()) > 0 ? 1 : -1;
            }
           
        });
        set.addAll(terms2.entrySet());
        
        return set.iterator();
    }
	

}
