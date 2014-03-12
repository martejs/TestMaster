package luceneindexer.search;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class SearchDBpedia {

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
				Hits treff = new Hits();
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				String resource = d.get("resource");

				Terms termVector = reader.getTermVector(docId, "shortAbstract");
				TermsEnum itr = termVector.iterator(null);
				BytesRef term = null;

				while ((term = itr.next()) != null) {  
					String termText = term.utf8ToString();
					treff.setTerms(termText);
					Term termInstance = new Term("shortAbstract", term);
					//Tror denne skriver ut frekvensen på termen i hele datasettet, og ikke bare i hits
					long termFreq = reader.totalTermFreq(termInstance);
					long docCount = reader.docFreq(termInstance);

//					System.out.println("term: "+termText+", termFreq = "+termFreq+", docCount = "+docCount);
				}

				

				//				Explanation explanation = searcher.explain(query, docId);
				//				System.out.println("------------");

				System.out.println("Hashmap:" + treff.getTerms()); 
				System.out.println("Funnet: " + hits[i].score + " med resource: " + resource);
				//				System.out.println("Funnet: " + resource);
				//				System.out.println(explanation.toString());



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
		//Field
		String label = "label";
		String field = "shortAbstract";
		String longField = "longAbstract";
		String sfField = "attr_tag";
		//Index folder
		String indexLabel = "indexDir";
		String indexPath = "indexShort";
		String indexLong = "indexLong";
		String indexSF = "SanFranciscoIndex";
		String indexSampleShort = "sampleShortIndex";

		//query to search

		//		System.out.print("Skriv inn sÔøΩkeord");
		String queryStr = "greek";

		int maxHits = 3;
		//		System.out.println("Label:");
		//		searchFiles(label, indexLabel, queryStr, maxHits);
		System.out.println("Short:");
		searchFiles(field, indexSampleShort, queryStr, maxHits);

		//		System.out.println("Long:");
		//		searchFiles(longField, indexLong, queryStr, maxHits);

	}

}