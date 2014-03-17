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
		Iterator i = valueIterator(terms);
		
		for(terms.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			
			int size = this.terms.size();
			
			this.tf = (float) value / size;
			this.idf = (float) (Math.log(size/value)); 
			this.tfidf = (tf*idf);
			
			System.out.println(key + ":" + value + " Tf/idf: " + this.tfidf);
			
		}
		return this.terms;
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
