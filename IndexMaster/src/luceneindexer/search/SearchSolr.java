package luceneindexer.search;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.ws.http.HTTPException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class SearchSolr {
	private SolrDocumentList result[]=null;

	public SolrDocumentList[] getDocLists() {
		return result;
	}
	public SearchSolr(SolrQuery query, SolrQuery term2) throws SolrServerException, HTTPException, IOException{
		System.out.println("Query er: " + query + " Term 2 er: " + term2);
		//	}

		ArrayList<String> list = new ArrayList<String>();
		list.add("WorldGEOUpcoming");

		HttpSolrServer server;

		String qyr;
		String qyr2;

		qyr = "attr_tag:" + query;
		qyr2 = "attr_tag:" + term2;
		int list_size = list.size();
		result = new SolrDocumentList[list_size];
		for (int i = 0; i < list_size; i++) {
			server= new HttpSolrServer("http://129.241.111.168:8983/solr/"+list.get(i));
			SolrQuery q = new SolrQuery(qyr + " AND "+ qyr2).setRows(100);
			QueryResponse qr;
			try {
				qr = server.query(q);
				result[i] = qr.getResults();

			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}

	}

}
