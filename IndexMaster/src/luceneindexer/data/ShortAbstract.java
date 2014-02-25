package luceneindexer.data;

//import java.io.IOException;

import luceneindexer.files.LuceneWriter;

//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.standard.StandardTokenizer;
//import org.apache.lucene.util.Version;

//import removeStopwords.StopWords;

public class ShortAbstract {

	private String short_abstract = "";
	private String resource = "";
//	private StopWords stop = new StopWords();
	LuceneWriter sw = new LuceneWriter();
	

	public String getShort() {
		return short_abstract;
	}

	public void setShort(String short_abstract) {
		if(short_abstract.length() > 4){
			sw.removeStopWords(short_abstract);	
		}
				
		this.short_abstract = short_abstract;
	}
	
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		
		this.resource = resource;
	}
	
	@Override
	public String toString() {
		return "ShortAbstract{" + "short_abstract=" + short_abstract + "resource=" + resource +'}';
	}
	
//	public void stopWordRemove(String short_abstract){
//	StandardAnalyzer ana = new StandardAnalyzer(Version.LUCENE_44);
//	TokenStream tk = new StandardTokenizer(Version.LUCENE_44, new StringReader(String));
//		
//	}

}
