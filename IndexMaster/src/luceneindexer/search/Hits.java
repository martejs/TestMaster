package luceneindexer.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;


public class Hits {
	
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<String>> documentFrequency = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> resourceList;
	private int occurrences = 0;
	private float tf = 0;
	private float idf = 0;
	private float tfidf = 0; 
	private float chiSquare = 0;
	private String first = "";
	
	public void setTerms(String term, String resource){
		if(!this.terms.containsKey(term)){
			
//			System.out.println("ny term: " + term + " Ressurs " + resource);
			
			
			this.occurrences = 1;
			terms.put(term, this.occurrences);
		}
		else{
			terms.put(term, terms.get(term) + 1);
		}
//		t.setDocumentFrequency(term, resource);
		
	}
	
	public HashMap<String, Integer> getTerms(){
		Iterator i = valueIterator(terms);
		List<String> ord = new ArrayList<String>();
		
		for(terms.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			ord.add(key);
			
			int size = this.terms.size();
			tfidf = getTfidf(value, size);
//			frequence = getHitsFrequence(key);
			System.out.println(key + " : " + value + " Tf/idf: " + this.tfidf  + " antall dok: " + getHitsFrequence(key));
			
			
		}
		first = ord.get(0); 
		return this.terms;
		
	}
	
	public int getFrequency(String term){
		
		int freq = getHitsFrequence(term);
		
		return freq;
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
	
	public int getHitsFrequence(String term){
		
		return documentFrequency.get(term).size();
	}

	public HashMap<String, ArrayList<String>> getDocumentFrequency() {
		return documentFrequency;
	}

	public void setDocumentFrequency(String term, String resource) {
		if(!documentFrequency.containsKey(term)){
			resourceList = new ArrayList<String>();
			resourceList.add(resource);
			documentFrequency.put(term, resourceList);
		}else if(documentFrequency.containsKey(term)){
			ArrayList<String> rl = documentFrequency.get(term);
			if(rl.contains(resource)){
			}
			else{
				// documentFrequency.get(term);
				// resourceList.add(resource);
				rl.add(resource);
				documentFrequency.put(term, rl);
//				System.out.println("2. Satt: " + term + " og ressurs: " + rl);
			}
		}
	}
	
	public float getChiSquare(int q, int all){
		float nNa = q/all;
		float square = getFrequency(first)*(1-nNa);
		chiSquare = square/q;
		return chiSquare;
		
	}

}
