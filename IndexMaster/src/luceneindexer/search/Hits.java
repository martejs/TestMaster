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
	private float mInfo=0;
	private List<String> ord = new ArrayList<String>();

	
	public void setTerms(String term, String resource){
		if(!this.terms.containsKey(term)){
			this.occurrences = 1;
			terms.put(term, this.occurrences);
		}
		else{
			terms.put(term, terms.get(term) + 1);
		}
	}
	

	/**
	 * 
	 * Calls the method for calculating Tf/Idf for each term 
	 * Calls the method for calculating Chi square for each pair (query, term2)
	 * Calls the method for calculating Mutual Information for each pair (query, term2)
	 * @return the relevant terms, DBpedia
	 */
	
	
	public HashMap<String, Integer> getTerms(){
		Iterator i = valueIterator(terms);

		for(terms.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			ord.add(key);
			if(value.intValue()>1){
				first = key;
				if(!SearchDBpedia.queryStr.equals(key)){
					getChiSquare(getFrequency(SearchDBpedia.queryStr), 4004477);
					//getMI(getFrequency(SearchDBpedia.queryStr), 4004477);
				}
			}
			
			int size = this.terms.size();
			tfidf = getTfidf(value, size);
			
			if(value.intValue()>1){
				System.out.println(key + " : " + value + " Tf/idf: " + this.tfidf  + " antall dok: " + getFrequency(key));
			}
		}
		for (int j = 0; j < ord.size(); j++) {
			if(ord.get(j).equals(SearchDBpedia.queryStr)){ 
				first = ord.get(j+1);
			}	
		}
		return this.terms;
	}

	/**
	 * @param term
	 * @return the frequency for the given term
	 */
	public int getFrequency(String term){
		int freq = documentFrequency.get(term).size();
		return freq;
	}

	/**
	 * Calculates tf/idf
	 * @param value - the term frequency 
	 * @param size - the number of terms in the index
	 * @return the Tf/ifd for the given term
	 */
	public float getTfidf(int value, int size){

		tf = (float) value / size;
		idf = (float) (Math.log(size/value)); 
		tfidf = (tf*idf);

		return tfidf;
	}

	/**
	 * Iterator method
	 */
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

	/**
	 * 
	 * @param term
	 * @param resource
	 */
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
		
			System.out.println("Sporreterm: " + SearchDBpedia.queryStr + " Term: " + first);
				float nNa = q/(float)all;

				float square =getFrequency(first)* (float) Math.pow(((1-nNa)),2);
				System.out.println("q=" + q +" square=" + square);
				chiSquare = square/q;
				System.out.println("sporreterm: " + q + " term2: " + getFrequency(first) + " blir chi-square: " + chiSquare);



				return chiSquare;

	}

	public float getMI(int q, int all){
		
		

		float nanb= q*getFrequency(first);
		float nab = tfidf;
		mInfo = nab/nanb;
		System.out.println("Sporreterm: " + SearchDBpedia.queryStr + "Soketerm: "+ first );
		System.out.println("Sporreterm: " + q + " term2: " + getFrequency(first) + " Mutual Information: " + mInfo) ;
		
		return mInfo;
		
	}




}
