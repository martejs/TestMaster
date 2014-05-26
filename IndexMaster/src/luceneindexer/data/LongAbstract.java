package luceneindexer.data;

import luceneindexer.files.LuceneWriter;


public class LongAbstract {
	
	private String long_abstract = "";
	private String resource = "";
	LuceneWriter sw = new LuceneWriter();
	
	
	
	public String getLongAbstract() {
		return long_abstract;
	}
	
	public void setLongAbstract(String longAbstract) {
		
		sw.setTotalDocs();
		String longAb = new String();
		
		//removes stopwords
		longAb = sw.removeStopWords(longAbstract);
		
		this.long_abstract = longAb;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	@Override
	public String toString() {
		return "LongAbstract{" + "long_abstract=" + long_abstract + "resource=" + resource +'}';
	}

	

}
