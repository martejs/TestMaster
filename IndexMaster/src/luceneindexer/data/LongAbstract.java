package luceneindexer.data;

import luceneindexer.files.LuceneWriter;


public class LongAbstract {
	
	private String long_abstract = "";
	private String resource = "";
	private int totalDocs = 0;
	LuceneWriter sw = new LuceneWriter();
	
	
	
	public String getLongAbstract() {
		return long_abstract;
	}
	
	public void setLongAbstract(String longAbstract) {
		setTotalDocs();
		String longAb = new String();
		
		
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

	public int getTotalDocs() {
		return totalDocs;
	}

	public void setTotalDocs() {
		totalDocs++;
	}

}
