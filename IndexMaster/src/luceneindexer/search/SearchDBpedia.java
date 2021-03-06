package luceneindexer.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.ws.http.HTTPException;

import luceneindexer.Main;
import luceneindexer.files.LuceneWriter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;



public class SearchDBpedia{


	static String queryStr;
	private SolrDocumentList[] results;
	private static List<String> urls;
	
	//Creates a SearchDBpedia object without query expansion
	public SearchDBpedia(String query){
		SearchSolr url = new SearchSolr();
		SolrQuery q = new SolrQuery(query);
		
		results = url.getUrls(q);
	
		
		 urls = new ArrayList<String>();
		 //Urls for the images are put in the list
		for (int i = 0; i < results.length; i++) {
			for (SolrDocument doc : results[i]) {
				urls.add((String) doc.getFieldValue("url_s"));
			}

		}
		
		
	}

	//Creates a SearchDBpedia object with query expansion
	public SearchDBpedia(String query, int valg){

		queryStr = query;
		String longField = "longAbstract";
		String indexLong = "LongIndex";
		int maxHits = 100;

		searchFiles(longField, indexLong, queryStr, maxHits, valg);

	}


	public static List<String> getUrls() {
		return urls;
	}
	
	//Method for searching in the index
	public static boolean searchFiles(String field, String indexPath, String queryStr, int maxHits, int valg){

		IndexReader reader;

		try {
			reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
			QueryParser parser = new QueryParser(Version.LUCENE_45, field, analyzer);
			Query query = parser.parse(queryStr);
			TopDocs topDocs = searcher.search(query, maxHits);
			ScoreDoc[] hits = topDocs.scoreDocs;



			Hits treff = new Hits(valg);
			LuceneWriter writer = Main.getLongWriter();

			for(int i = 0; i < hits.length; i++){
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				String resource = d.get("resource");
				String longAbstract = d.get("longAbstract");

				Scanner sc = new Scanner(longAbstract);
				while(sc.hasNext()){
					String nextTerm = sc.next();
					treff.setDocumentFrequency(nextTerm, resource);
					treff.setTerms(nextTerm, resource);
				}
			}
			try {
				treff.getTerms();
			} catch (HTTPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			urls = treff.getUrls();

			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}


}
