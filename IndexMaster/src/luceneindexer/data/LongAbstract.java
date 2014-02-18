package luceneindexer.data;

import removeStopwords.StopWords;

public class LongAbstract {
	
	private String long_abstract = "";
	private String resource = "";
	StopWords stop = new StopWords();
	
	
	
	public String getLongAbstract() {
		return long_abstract;
	}
	
	public void setLongAbstract(String longAbstract) {
		
		this.long_abstract = longAbstract;
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
