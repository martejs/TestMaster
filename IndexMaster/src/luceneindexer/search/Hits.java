package luceneindexer.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.ws.http.HTTPException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;


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
	private List<Term2> term2List = new ArrayList<Term2>();
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

		// for(terms.entrySet().iterator(); i.hasNext();){
		for (Entry<String, Integer> e:terms.entrySet()) {
			// Map.Entry entry = (Map.Entry) i.next();
			// String key = (String) entry.getKey();
			// Integer value = (Integer) entry.getValue();
			String key = e.getKey();
			Integer value = e.getValue();
			ord.add(key);
			
			term2 = new Term2(SearchDBpedia.queryStr, key, 4004477);
			int size = this.terms.size();
			term2.setTfIdf(getTfidf(value, size));
			
			
				first = key;
				if(!SearchDBpedia.queryStr.equals(key)){
					term2.setChi(getChiSquare(getFrequency(SearchDBpedia.queryStr), 4004477));
					term2.setMI(getMI(getFrequency(SearchDBpedia.queryStr), 4004477));
				}
			
			
			
//			if(value.intValue()>1){
//				System.out.println(key + " : " + value + " Tf/idf: " + this.tfidf  + " antall dok: " + getFrequency(key));
//			}
			term2List.add(term2);
		}
		for (int j = 0; j < ord.size(); j++) {
			if(ord.get(j).equals(SearchDBpedia.queryStr)){ 
				first = ord.get(j+1);
			}	
		}
		
		float tfTest = 0;
		float miTest = 0;
		float chiTest = 0;
		
		SolrQuery term2TF = new SolrQuery();
		SolrQuery term2MI = new SolrQuery();
		SolrQuery term2Chi = new SolrQuery();
		SolrQuery term1 = new SolrQuery();
		
		for (int j = 1; j < term2List.size(); j++) {
			if(tfTest < term2List.get(j).getTfIdf()){
				tfTest = term2List.get(j).getTfIdf();
				term2TF = new SolrQuery(term2List.get(j).getTerm2());
				term1 = new SolrQuery(term2List.get(j).getTerm1());
				System.out.println("TF2: " + tfTest + " term2: " + term2TF);
			}
			if(miTest < term2List.get(j).getMI()){
				miTest = term2List.get(j).getMI();
				term2MI = new SolrQuery(term2List.get(j).getTerm2());
				term1 = new SolrQuery(term2List.get(j).getTerm1());
				System.out.println("MI2: " + miTest + " term2: " + term2MI);
			}
			if(chiTest < term2List.get(j).getChi()){
				chiTest = term2List.get(j).getChi();
				term2Chi = new SolrQuery(term2List.get(j).getTerm2());
				term1 = new SolrQuery(term2List.get(j).getTerm1());
				System.out.println("Chi2: " + chiTest + " term2: " + term2Chi);
			}
//			System.out.println("Term2:" + term2List.get(j).getTerm2());
//			System.out.println("Chi: "+ term2List.get(j).getChi());
//			System.out.println("MI; " + term2List.get(j).getMI());
//			System.out.println("Tf-Idf: "+ term2List.get(j).getTfIdf());
//			System.out.println("-----------");
//			
			
		}
		
		switch (method){
			case 1: method=1;
				SearchSolr searchSolr = new SearchSolr(term1, term2TF);
				System.out.println("Case 1");
				break;
			case 2: method=2;
				SearchSolr searchSolr2 = new SearchSolr(term1, term2MI);
				System.out.println("Case 2");
				break;
			case 3: method=3;
				SearchSolr searchSolr3 = new SearchSolr(term1, term2Chi);
				System.out.println("Case 3");
				break;
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
		
//			System.out.println("Sporreterm: " + SearchDBpedia.queryStr + " Term: " + first);


				float nNa = q/(float)all;

				float square =getFrequency(first)* (float) Math.pow(((1-nNa)),2);
				chiSquare = square/q;

//				System.out.println("spørreterm: " + q + " term2: " + getFrequency(first) + " blir chi-square: " + chiSquare);




				return chiSquare;

	}

	public float getMI(float q, int all){
		float nanb = q*getFrequency(first);
		float nab = tfidf;
		mInfo = nab/nanb;

//		System.out.println("Spørreterm: " + SearchDBpedia.queryStr + "Søketerm: "+ first );

		
//		System.out.println("Sporreterm: " + q + " term2: " + getFrequency(first) + " Mutual Information: " + mInfo) ;

		return mInfo;
		
	}




}
