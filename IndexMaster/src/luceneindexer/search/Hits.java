package luceneindexer.search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


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
		Iterator i = valueIterator(terms);
		while(i.hasNext()){
			System.out.println(i.next());
		}
		return this.terms;
	}
	
	
	Iterator valueIterator(HashMap<String, Integer> terms2) {
        Set set = new TreeSet(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return  o1.getValue().compareTo(o2.getValue()) > 0 ? 1 : -1;
            }
        });
        set.addAll(terms2.entrySet());
        return set.iterator();
    }

}
