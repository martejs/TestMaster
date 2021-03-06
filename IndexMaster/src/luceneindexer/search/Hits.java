package luceneindexer.search;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.ws.http.HTTPException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class Hits {

	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<String>> documentFrequency = new HashMap<String, ArrayList<String>>();
	private HashMap<String, Float> returnedByTfIdf = new HashMap<String, Float>();
	private ArrayList<String> resourceList;
	private int occurrences = 0;
	private float tf = 0;
	private float idf = 0;
	private float tfidf = 0; 
	private float chiSquare = 0;
	private float mInfo=0;
	private List<Term2> term2List = new ArrayList<Term2>();

	private SolrDocumentList[] results;
	Term2 term2;

	private int method;

	public Hits(int metode){
		method = metode;
	}

	//Method for finding the term frequence to each term. This is put in a map. 
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

		for (Entry<String, Integer> e:terms.entrySet()) {
			String key = e.getKey();
			Integer value = e.getValue();


			term2 = new Term2(SearchDBpedia.queryStr, key, 4004477);
			int size = this.terms.size();
			tfidf = getTfidf(value, size);
			term2.setTfIdf(tfidf);

			returnedByTfIdf.put(key, tfidf);
			term2List.add(term2);

		}

		List<Term2> candidates = getCandidates();

		float miTest = 0;
		float chiTest = 0;

		SolrQuery term = new SolrQuery();
		SolrQuery term1 = new SolrQuery();

		
		
		for (int j = 0; j < candidates.size(); j++) {
			//Finding the term with the highest Mutual information
			if(method == 2){
				if(miTest < candidates.get(j).getMI() && !(candidates.get(j).getTerm1().equals(candidates.get(j).getTerm2()))){
					miTest = candidates.get(j).getMI();
					term = new SolrQuery(candidates.get(j).getTerm2());
					term1 = new SolrQuery(candidates.get(j).getTerm1());
					System.out.println("MI2: " + miTest + " term2: " + term);					
				}
			}
			
			//Finding the term with the highest Chi-square
			if(method == 3){
				if(chiTest < candidates.get(j).getChi() && !(candidates.get(j).getTerm1().equals(candidates.get(j).getTerm2()))){
					chiTest = candidates.get(j).getChi();
					term = new SolrQuery(candidates.get(j).getTerm2());
					term1 = new SolrQuery(candidates.get(j).getTerm1());
					System.out.println("Chi2: " + chiTest + " term2: " + term);	
				}else{
					System.out.println("Chi2: " + candidates.get(j).getChi() + " term2: " + candidates.get(j).getTerm2());
				}
				
			}
		}

		SearchSolr searchSolr = null;

		//Switch for the different methods, and creates a SearchSolr object for querying Solr.
		switch (method){
		case 1:
			System.out.println("Case 1");
			System.out.println("TF2: " + candidates.get(0).getTerm2() + " term2: " + candidates.get(0).getTfIdf());
			term = new SolrQuery(candidates.get(0).getTerm2());
			term1 = new SolrQuery(candidates.get(0).getTerm1());
			searchSolr = new SearchSolr(term1, term);

			//The results from Solr
			results = searchSolr.getDocLists();
			break;
		case 2:
			System.out.println("Case 2");
			searchSolr = new SearchSolr(term1, term);
			//The results from Solr
			results = searchSolr.getDocLists();
			break;
		case 3:
			System.out.println("Case 3");
			searchSolr = new SearchSolr(term1, term);
			//The results from Solr
			results = searchSolr.getDocLists();
			break;
		}
		return this.terms;
	}

	public List<String> getUrls() {
		List<String> urls = new ArrayList<String>();
		// Fills the list with url's for the images
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


	//Creates the list for candidates for query expansion, only the 100 best
	public List<Term2> getCandidates(){
		Collections.sort(term2List);

		List <Term2> resultList = new ArrayList<Term2>(100);

		int x = 0;

		for (Term2 to : term2List) {
			String name = to.getTerm2();

			if(!SearchDBpedia.queryStr.equals(name)){
				x++;
				to.setChi(getChiSquare(getFrequency(SearchDBpedia.queryStr), 100, name));
				to.setMI(getMI(getFrequency(SearchDBpedia.queryStr), name));
				resultList.add(to);
			}

			if(x==150) break;
		}

		return resultList;

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
	
	/**
	 * @param q: the number of occurrences of the query term
	 * @param all: number of terms returned from TF-IDF
	 * @param word: the candidate for query expansion
	 * nNa: the number of occurrences of q divided by the 100 best terms returned by Tf/IDF.   
	 * square: the frequency of the second term multiplied with (1-nNa) squared 
	 * @return chiSquare
	 */
	public float getChiSquare(int q, int all, String word){
		float nNa = q/(float)all;
		float square =getFrequency(word)* (float) Math.pow(((1-nNa)),2);
		chiSquare = square/q;  


		return chiSquare;

	}

	/**
	 * @param q: number of occurrences of the query term
	 * @param word: the candidate for query expansion
	 * nanb: the number of documents that the query occurs in * the number of documents that the second term occur in
	 * nab: the number of documents that the query and the second term occur in
	 * @return Mutual information 
	 */
	public float getMI(float q, String word){
		float nanb = q*getFrequency(word);
		float nab = returnedByTfIdf.size();
		mInfo = nab/nanb;

		return mInfo;

	}


}
