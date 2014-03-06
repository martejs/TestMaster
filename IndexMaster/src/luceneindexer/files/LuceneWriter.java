package luceneindexer.files;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import luceneindexer.data.DBpedia;
import luceneindexer.data.LongAbstract;
import luceneindexer.data.ShortAbstract;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneWriter {
    
    String pathToIndex = "";
    IndexWriter indexWriter = null;
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);

    public LuceneWriter() {
    }

    public LuceneWriter(String pathToIndex) {
        this.pathToIndex = pathToIndex;
    }
    
    public boolean openIndex(){
        
        
        try {
            
            //Open the directory so lucene knows how to deal with it
            Directory dir = FSDirectory.open(new File(pathToIndex));
            
            //Chose the analyzer we are going to use to write documents to the index. We need to specify the version 
            //of the Lucene index type we want to use
//            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
            
            
            //Create an index writer configuration. Same thing here with the index version
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, analyzer);
            
            //we are always going to overwrite the index that is currently in the directory
            iwc.setOpenMode(OpenMode.CREATE);
            
            //let's open that index and get a writer to hand back to the main code
            indexWriter = new IndexWriter(dir, iwc);
            
            return true;
        } catch (Exception e) {
            System.out.println("Threw an exception trying to open the index for writing: " + e.getClass() + " :: " + e.getMessage());
            return false;
        }
                
    }
    
//    public void addDBpedia(DBpedia dBpedia){
//    	FieldType fieldType = new FieldType();
//        fieldType.setStoreTermVectors(true);
//        fieldType.setStoreTermVectorPositions(true);
//        fieldType.setIndexed(true);
//        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
//        fieldType.setStored(true);
//        
//        Document doc = new Document();
//        doc.add(new Field("label", dBpedia.getLabel(), fieldType));
//        doc.add(new Field("resource", dBpedia.getResource(), fieldType));
//        try {
//            indexWriter.addDocument(doc);
//        } catch (IOException ex) {
//            System.out.println("Threw an exception trying to add the doc: " + ex.getClass() + " :: " + ex.getMessage());
//        }
//        
//    }
    
    public void addShortAbstract(ShortAbstract shortAbstract){
    	FieldType fieldType = new FieldType();
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setIndexed(true);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(true);
        
        //Har du fŒtt denne?
    	
        Document doc = new Document();
        doc.add(new Field("shortAbstract", shortAbstract.getShort(), fieldType));
        doc.add(new Field("resource", shortAbstract.getResource(), fieldType));
        try {
            indexWriter.addDocument(doc);
        } catch (IOException ex) {
            System.out.println("Threw an exception trying to add the doc: " + ex.getClass() + " :: " + ex.getMessage());
        }
        
    }
   
//    public void addLongAbstract(LongAbstract longAbstract){
//        Document doc = new Document();
//        doc.add(new TextField("longAbstract", longAbstract.getLongAbstract(), Field.Store.YES));
//        doc.add(new TextField("resource", longAbstract.getResource(), Field.Store.YES));
//        try {
//            indexWriter.addDocument(doc);
//        } catch (IOException ex) {
//            System.out.println("Threw an exception trying to add the doc: " + ex.getClass() + " :: " + ex.getMessage());
//        }
//        
//    }
    
    
    public void finish(){
        System.out.println("about to close the writer");
        try {
            //commit the document to the index
            indexWriter.commit();
            
            //now close it off and release the lock
            indexWriter.close();
        } catch (IOException ex) {
            System.out.println("We had a problem closing the index: " + ex.getClass() + " :: " + ex.getLocalizedMessage());
        }
    }
    
    
    public String removeStopWords(String sentence){

    	Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_45, new StringReader(sentence));
    	final StandardFilter standardFilter = new StandardFilter(Version.LUCENE_45, tokenizer);
    	final StopFilter stopFilter = new StopFilter(Version.LUCENE_45, standardFilter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
    	final CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
    	
    	StringBuilder sb = new StringBuilder();
    	
    	try {
			stopFilter.reset();
			
			while(stopFilter.incrementToken()){				
				if (sb.length() > 0) {
                    sb.append(" ");
                }
				final String token = charTermAttribute.toString().toString();
				sb.append(token);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return sb.toString();
    	
    	
    	
    	
    	
    }
    
    
    
    
    
    
}
