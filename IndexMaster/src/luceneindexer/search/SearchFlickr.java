package luceneindexer.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

public class SearchFlickr {
	
public static boolean searchFiles(String field, String indexPath, String queryStr, int maxHits){
		
		IndexReader reader;
		
		try {
			reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
			QueryParser parser = new QueryParser(Version.LUCENE_45, field, analyzer);
			Query query = parser.parse(queryStr);
			TopDocs topDocs = searcher.search(query, maxHits);
			ScoreDoc[] hits = topDocs.scoreDocs;
			
			for(int i = 0; i < hits.length; i++){
				System.out.println("Inne i for");
				
//				int docId = hits[i].doc;
//				Document d = searcher.doc(docId);
//				String resource = d.get("resource");
				

//				Explanation explanation = searcher.explain(query, docId);
//				System.out.println("------------");

				System.out.println("Funnet: " + hits[i]);
//				System.out.println("Funnet: " + resource);
//				System.out.println(explanation.toString());


			}
			System.out.println("Fant " + hits.length);
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException her");
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException her");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static void main(String[] args){
		//Field
		String tag = "attr_tag";
		//Index folder

		String index = "/Users/martesaether/Downloads/WorldGEOUpcoming 2/data/index";

		//query to search
		String queryStr = "california";
		
		int maxHits = 5;
		System.out.println("Flickr:");
		searchFiles(tag, index, queryStr, maxHits);
	}

}
