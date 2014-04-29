package luceneindexer.search;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.ws.http.HTTPException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class SearchSolr {
	private SolrDocumentList result[]=null;

	public SolrDocumentList[] getDocLists() {
		return result;
	}
	public SearchSolr(SolrQuery query, SolrQuery term2) throws SolrServerException, HTTPException, IOException{
		System.out.println("Query er: " + query + " Term 2 er: " + term2);
//	}
//	public static void main(String [] args) throws SolrServerException, HTTPException, IOException {

		//		String url = "http://129.241.111.168:8983/solr/#/";
		ArrayList<String> list = new ArrayList<String>();
//		list.add("SanFrancisco");
//		list.add("Mediaeval");
//		list.add("London");
//		list.add("POOLFutebal");
//		list.add("Upcoming");
		list.add("WorldGEOUpcoming");
		//list.add("WorldTiles");

		HttpSolrServer server;
//		String input= JOptionPane.showInputDialog("s�keord");

		//String [] splitStrings = input.split(" ");
		String qyr;
		String qyr2;

		qyr = "attr_tag:" + query;
		qyr2 = "attr_tag:" + term2;
		//+ splitStrings[0];
		//qyr2 = "attr_tag:" + splitStrings[1];
		int list_size = list.size();
		result = new SolrDocumentList[list_size];
		for (int i = 0; i < list_size; i++) {
			server= new HttpSolrServer("http://129.241.111.168:8983/solr/"+list.get(i));

			SolrQuery q = new SolrQuery(qyr + " AND "+ qyr2).setRows(100);
//			q.setQuery("num:[0 TO 20]").setSortField("num", ORDER.desc).setRows(100);

//			q = new SolrQuery(qyr);

			QueryResponse qr;
			try {
				qr = server.query(q);
				result[i] = qr.getResults();

				//MPServlet servlet = new MPServlet(result);

//				for (SolrDocument doc : result) {
//					Object id = doc.getFieldValue("url_s");
//					ArrayList name = (ArrayList) doc.getFieldValue("attr_tag");
//					System.out.println(id + " " + name);
//				}
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 



		}














	}

}