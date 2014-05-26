package luceneindexer.files;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import luceneindexer.data.DBpedia;
import luceneindexer.data.LongAbstract;
import luceneindexer.data.ShortAbstract;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.ReaderManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneWriter {

	String pathToIndex = "";
	IndexWriter indexWriter = null;
	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
	private static int totalDocs;



	public LuceneWriter() {
	}

	public LuceneWriter(String pathToIndex) {
		this.pathToIndex = pathToIndex;
	}

	public boolean openIndex(){

		try {

			//Open the directory so Lucene knows how to deal with it
			Directory dir = FSDirectory.open(new File(pathToIndex));

			//Create an index writer configuration. 
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, analyzer);

			//Overwrites the index that is currently in the directory
			iwc.setOpenMode(OpenMode.CREATE);

			//Open index and get a writer to hand back to the main code
			indexWriter = new IndexWriter(dir, iwc);

			return true;
		} catch (Exception e) {
			System.out.println("Threw an exception trying to open the index for writing: " + e.getClass() + " :: " + e.getMessage());
			return false;
		}

	}
	
	//Code for indexing Title and Short abstract in DBpeida

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
	//    
	//    public void addShortAbstract(ShortAbstract shortAbstract){
	//    	
	//    	FieldType fieldType = new FieldType();
	//        fieldType.setStoreTermVectors(true);
	//        fieldType.setStoreTermVectorPositions(true);
	//        fieldType.setIndexed(true);
	//        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
	//        fieldType.setStored(true);
	//        
	//
	//    	
	//        Document doc = new Document();
	//        doc.add(new Field("shortAbstract", shortAbstract.getShort(), fieldType));
	//        doc.add(new Field("resource", shortAbstract.getResource(), fieldType));
	//        try {
	//            indexWriter.addDocument(doc);
	//        } catch (IOException ex) {
	//            System.out.println("Threw an exception trying to add the doc: " + ex.getClass() + " :: " + ex.getMessage());
	//        }
	//        
	//    }

	//Code for indexing long abstract
	public void addLongAbstract(LongAbstract longAbstract){
		FieldType fieldType = new FieldType();
		fieldType.setStoreTermVectors(true);
		fieldType.setStoreTermVectorPositions(true);
		fieldType.setIndexed(true);
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		fieldType.setStored(true);
		Document doc = new Document();
		doc.add(new Field("longAbstract", longAbstract.getLongAbstract(), fieldType));
		doc.add(new Field("resource", longAbstract.getResource(), fieldType));
		try {
			indexWriter.addDocument(doc);
		} catch (IOException ex) {
			System.out.println("Threw an exception trying to add the doc: " + ex.getClass() + " :: " + ex.getMessage());
		}

	}

	public int getTotalDocs() {
		System.out.println("getTotal?"+this.totalDocs);
		return this.totalDocs;
	}

	public void setTotalDocs() {
		this.totalDocs= getTotalDocs()+1;
	
	}


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

	//Removing stop word
	public String removeStopWords(String sentence){

		sentence= sentence.toLowerCase();
		TokenStream ts = new StandardTokenizer(Version.LUCENE_45, new StringReader(sentence));

		ts = new StopFilter(Version.LUCENE_45, ts, StandardAnalyzer.STOP_WORDS_SET);
		final List<String> stopWords = Arrays.asList("able","about","above","abst","accordance","according","accordingly","across",
				"act","actually","added","affected","affecting","affects","after","afterwards","again","against","ah","all","almost",
				"alone","along","already","also","although","always","am","among","amongst","an","and","announce","another","any","anybody",
				"anyhow","anymore","anyone","anything","anyway","anyways","anywhere","apparently","approximately","are","aren","arent","arise",
				"around","as","aside","ask","asking","at","auth","available","away","awfully","back","be","became","because","become",
				"becomes","becoming","been","before","beforehand","begin","beginning","beginnings","begins","behind","being","believe",
				"below","beside","besides","between","beyond","biol","both","brief","briefly","but","by","ca","came","can","cannot",
				"can't","cause","causes","certain","certainly","co","com","come","comes","contain","containing","contains","could",
				"couldnt","date","did","didn't","different","do","does","doesn't","doing","done","don't","down","downwards","due",
				"during","each","ed","edu","effect","eg","eight","eighty","either","else","elsewhere", "en","end","ending","enough",
				"especially","et","et-al","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","except",
				"far","few","ff","fifth","first","five","fix","followed","following","follows","for","former","formerly","forth","found",
				"four","from","further","furthermore","gave","get","gets","getting","give","given","gives","giving","go","goes","gone",
				"got","gotten","had","happens","hardly","has","hasn't","have","haven't","having","he","hed","hence","her","here",
				"hereafter","hereby","herein","heres","hereupon","hers","herself","hes","hi","hid","him","himself","his","hither","home",
				"how","howbeit","however","hundred","id","ie","if","i'll","im","immediate","immediately","importance","important","in",
				"inc","indeed","index","information","instead","into","invention","inward","is","isn't","it","itd","it'll","its","itself",
				"i've","j","just","keep","keeps","kept","kg","km","know","known","knows","largely","last","lately","later","latter",
				"latterly","least","less","lest","let","lets","like","liked","likely","line","little","'ll","look","looking","looks","ltd",
				"made","mainly","make","makes","many","may","maybe","me","mean","means","meantime","meanwhile","merely","mg","might","million",
				"miss","ml","more","moreover","most","mostly","mr","mrs","much","mug","must","my","myself","n","na","name","namely","nay","nd",
				"near","nearly","necessarily","necessary","need","needs","neither","never","nevertheless","new","next","nine","ninety","no",
				"nobody","non","none","nonetheless","noone","nor","normally","nos","not","noted","nothing","now","nowhere","o","obtain","obtained",
				"obviously","of","off","often","oh","ok","okay","old","omitted","on","once","one","ones","only","onto","or","ord","other","others",
				"otherwise","ought","our","ours","ourselves","out","outside","over","overall","owing","own","p","page","pages","part","particular",
				"particularly","past","per","perhaps","placed","please","plus","poorly","possible","possibly","potentially","predominantly","present",
				"previously","primarily","probably","promptly","proud","provides","put","q","que","quickly","quite","qv","r","ran","rather","rd","re",
				"readily","really","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively",
				"resulted","resulting","results","right","run","s","said","same","saw","say","saying","says","sec","section","see","seeing","seem",
				"seemed","seeming","seems","seen","self","selves","sent","seven","several","shall","she","shed","she'll","shes","should","shouldn't",
				"show","showed","shown","showns","shows","significant","significantly","similar","similarly","since","six","slightly","so","some",
				"somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically",
				"specified","specify","specifying","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest",
				"sup","sure","t","take","taken","taking","tell","tends","than","thank","thanks","thanx","that","that'll","thats","that've","The",
				"the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","thered","therefore","therein","there'll",
				"thereof","therere","theres","thereto","thereupon","there've","these","they","theyd","they'll","theyre","they've","think","this",
				"those","thou","though","thoughh","thousand","throug","through","throughout","thru","thus","til","tip","to","together","too","took",
				"toward","towards","tried","tries","truly","try","trying","ts","twice","two","u","un","under","unfortunately","unless","unlike",
				"unlikely","until","unto","up","upon","ups","us","use","used","useful","usefully","usefulness","uses","using","usually","value",
				"various","'ve","very","via","viz","vol","vols","vs","w","want","wants","was","wasn't","way","we","wed","welcome","we'll","went",
				"were","weren't","we've","what","whatever","what'll","whats","when","whence","whenever","where","whereafter","whereas","whereby",
				"wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","whod","whoever","whole","who'll","whom",
				"whomever","whos","whose","why","widely","willing","wish","with","within","without","won't","words","world","would","wouldn't","www",
				"yes","yet","you","youd","you'll","your","youre","yours","yourself","yourselves","you've","zero");
		ts = new StopFilter(Version.LUCENE_45, ts, StopFilter.makeStopSet(Version.LUCENE_45, stopWords));

		
		StringBuilder sb = new StringBuilder();

		final CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);   


		try {
			ts.reset();
			//    		System.out.println("titt");
			while(ts.incrementToken()){

				//				System.out.println("titt2");
				if(sb.length()>0){
					sb.append(" ");

				}
				sb.append(charTermAttribute.toString());
				//				System.out.println(sb);

			}
		} catch (IOException e) {
			//			System.out.println("fail");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();

	}








}
