package luceneindexer.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermHits {
	
	private HashMap<String,ArrayList<String>> documentFrequency = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> resourceList = new ArrayList<String>();
	
	
//	public List<String> getResourceList(String term) {
//		documentFrequency.get(term);
//		return resourceList;
//	}
	
	
//	public void setResourceList(String r, String w) {
//		this.word = w;
//		if(!resourceList.contains(r)){
////			System.out.println("inne i if: " + r + "ord: " + word);
//			this.resourceList.add(r);
//			
//		}
//		else{
////			System.out.println("utenfor if, ikke lagt til: " + r + "ord: " + word);
//		}
//
//	}
	
	public int getResourceListLength(String term){
		List<String> list = documentFrequency.get(term);

		return list.size();
				
		
	}
	
	public void setDocumentFrequency(String term, String resource){
		if(!this.documentFrequency.containsKey(term)){
			
			System.out.println("ny term: " + term + " Ressurs" + resource);
			
			this.resourceList.add(resource);
			documentFrequency.put(term, resourceList);
		}
	}
	
	public HashMap<String, ArrayList<String>> getDocumentFrequency(){
		return documentFrequency;
	}
	

}
