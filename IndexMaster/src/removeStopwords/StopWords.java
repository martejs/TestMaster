package removeStopwords;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class StopWords {

	private static StopWords instance;
	private static final String stopwordsPath = "resource/stopwords.txt";
	private List<String> stopwords;

	public StopWords() {
		try {
			stopwords = Files.readAllLines(Paths.get(stopwordsPath), Charset.defaultCharset());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static StopWords getInstance() {
		if(instance == null)
			instance = new StopWords();
		return instance;
	}

	public String removeStopwords(String sentence) {
		for(String stopWord : stopwords) {
			
			sentence = sentence.replaceAll("\\s+" + stopWord + "\\s+", " ");
			sentence = sentence.replaceAll("^" + stopWord + "\\s+", "");
			sentence = sentence.replaceAll("\\s+" + stopWord + "$", "");
		}
		return sentence;
	}

//	public List<String> removeStopwords(List<String> sentences) {
//		List<String> newList = new ArrayList<String>();
//		for(String sentence : sentences) {
//			
//			String newSentence = "";
//
//			for(String stopWord : stopwords) {
//				newSentence = sentence.replaceAll(" " + stopWord + " ", " ");
//			}
//			newList.add(newSentence);
//		}
//		return newList;
//	}
}