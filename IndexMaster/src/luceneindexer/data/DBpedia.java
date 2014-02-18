package luceneindexer.data;


public class DBpedia {
    
    private String label = "";
    private String resource = "";
    


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

        

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
    
	@Override
	public String toString() {
		return "DBpedia{" + "label="+ label + "resource=" + resource +'}';
	}
}
