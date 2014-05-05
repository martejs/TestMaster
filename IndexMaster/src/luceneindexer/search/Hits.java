package luceneindexer.search;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.ws.http.HTTPException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;






public class Hits {

	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<String>> documentFrequency = new HashMap<String, ArrayList<String>>();
	private HashMap<String, Float> returnedByTfIdf = new HashMap<String, Float>();
	private HashMap<String, Float> tfidf150 = new HashMap<String, Float>();
	private ArrayList<String> resourceList;
	private int occurrences = 0;
	private float tf = 0;
	private float idf = 0;
	private float tfidf = 0; 
	private float chiSquare = 0;
	private String first = "";
	private float mInfo=0;
	private List<String> word = new ArrayList<String>();
	private List<Term2> term2List = new ArrayList<Term2>();
	private List<Term2> term2tf = new ArrayList<Term2>();

	private SolrDocumentList[] results;
	Term2 term2;

	private int method;

	public Hits(int metode){
		method = metode;
	}

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
	 * @throws IOException 
	 * @throws SolrServerException 
	 * @throws HTTPException 
	 */


	public HashMap<String, Integer> getTerms() throws HTTPException, SolrServerException, IOException{
		Iterator i = valueIterator(terms);

		for (Entry<String, Integer> e:terms.entrySet()) {
			String key = e.getKey();
			Integer value = e.getValue();
			

			term2 = new Term2(SearchDBpedia.queryStr, key, 4004477);
			int size = this.terms.size();
			tfidf = getTfidf(value, size);
			term2.setTfIdf(tfidf);
			
			returnedByTfIdf.put(key, tfidf);


			
			if(!SearchDBpedia.queryStr.equals(key)){
				term2.setChi(getChiSquare(getFrequency(SearchDBpedia.queryStr), tfidf150.size()));
				term2.setMI(getMI(getFrequency(SearchDBpedia.queryStr), tfidf150.size()));
			}


			tfidfSorting();
			term2List.add(term2);
		}
		

		float tfTest = 0;
		float miTest = 0;
		float chiTest = 0;

		SolrQuery term2TF = new SolrQuery();
		SolrQuery term2MI = new SolrQuery();
		SolrQuery term2Chi = new SolrQuery();
		SolrQuery term1 = new SolrQuery();

		for (int j = 1; j < term2List.size(); j++) {
			if(tfTest < term2List.get(j).getTfIdf()&& !(term2List.get(j).getTerm1().equals(term2List.get(j).getTerm2()))){
				tfTest = term2List.get(j).getTfIdf();
				term2TF = new SolrQuery(term2List.get(j).getTerm2());
				term1 = new SolrQuery(term2List.get(j).getTerm1());
				System.out.println("TF2: " + tfTest + " term2: " + term2TF);
			}
			if(miTest < term2List.get(j).getMI()&& !(term2List.get(j).getTerm1().equals(term2List.get(j).getTerm2()))){
				miTest = term2List.get(j).getMI();
				term2MI = new SolrQuery(term2List.get(j).getTerm2());
				term1 = new SolrQuery(term2List.get(j).getTerm1());
				System.out.println("MI2: " + miTest + " term2: " + term2MI);
			}
			if(chiTest < term2List.get(j).getChi()&& !(term2List.get(j).getTerm1().equals(term2List.get(j).getTerm2()))){
				chiTest = term2List.get(j).getChi();
				term2Chi = new SolrQuery(term2List.get(j).getTerm2());
				term1 = new SolrQuery(term2List.get(j).getTerm1());
				System.out.println("Chi2: " + chiTest + " term2: " + term2Chi);
			}
		}
		SearchSolr searchSolr = null;
		switch (method){
		case 1: method=1;
		searchSolr = new SearchSolr(term1, term2TF);
		results = searchSolr.getDocLists();
		System.out.println("Case 1");
		break;
		case 2: method=2;
		searchSolr = new SearchSolr(term1, term2MI);
		results = searchSolr.getDocLists();
		System.out.println("Case 2");
		break;
		case 3: method=3;
		searchSolr = new SearchSolr(term1, term2Chi);
		results = searchSolr.getDocLists();
		System.out.println("Case 3");
		break;
		}
		return this.terms;
	}

	public List<String> getUrls() {
		List<String> urls = new ArrayList<String>();
		// Fyller lista med url'er for bildene
		for (int i = 0; i < results.length; i++) {
			for (SolrDocument doc : results[i]) {
				urls.add((String) doc.getFieldValue("url_s"));
			}

		}

		return urls;
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
	
	
	public void tfidfSorting(){
		
		returnedByTfIdf = (HashMap<String, Float>) sortByValue(returnedByTfIdf);
		
		
		Iterator i = tfidfIterator(returnedByTfIdf);
		int x = 0;
		
		for (Entry<String, Float> e:returnedByTfIdf.entrySet()) {
			x+= 1;
			String key = e.getKey();
			Float value = e.getValue();
			tfidf150.put(key, value);
			word.add(key);
			
			term2tf.add(term2List.get);
			
						if(x==150) break;
		}
		
		for (int j = 0; j < word.size(); j++) {
			if(word.get(j).equals(SearchDBpedia.queryStr)){ 
				first = word.get(j+1);
			}	
		}

		System.out.println("liste "+ tfidf150);
		
	}
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ){
    List<Map.Entry<K, V>> list =
        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
            return (o2.getValue()).compareTo( o1.getValue() );
        }
    } );

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}
	
	Iterator tfidfIterator(HashMap<String, Float> tfidfList) {
		Set set = new TreeSet(new Comparator<Map.Entry<String, Float>>() {


			@Override
			public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
				return  o2.getValue().compareTo(o1.getValue()) > 0 ? 1 : -1;
			}
			
		});
		set.addAll(tfidfList.entrySet());

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
				rl.add(resource);
				documentFrequency.put(term, rl);
			}
		}
	}

	public float getChiSquare(int q, int all){
		float nNa = q/(float)all;
		float square =getFrequency(first)* (float) Math.pow(((1-nNa)),2);
		chiSquare = square/q;
		

		return chiSquare;

	}

	/**
	 * @param q: number of occurrences of the query term
	 * @param all: number of terms returned from tf/idf  
	 * @return Mutual information 
	 */
	public float getMI(float q, int all){
		float nanb = q*getFrequency(first);
		float nab = tfidf;
		mInfo = nab/nanb;

		return mInfo;

	}

}
