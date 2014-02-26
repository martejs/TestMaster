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

public class SearchDBpedia {
	
	public static boolean searchFiles(String indexPath, String queryStr, int maxHits){
		String field = "shortAbstract";
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
				//Vet ikke hva vi skal gj¿re her
				System.out.println("Funnet: " + hits[i]);
			}
			System.out.println("Fant " + hits.length);
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
	
	public static void main(String[] args){
		//Index folder
		String indexPath = "indexShort";
		//query to search
//		System.out.print("Skriv inn s¿keord");
		String queryStr = "Greek";
		
		int maxHits = 100;
		searchFiles(indexPath, queryStr, maxHits);
	}

}
