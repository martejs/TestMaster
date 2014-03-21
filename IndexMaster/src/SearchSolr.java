import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.RowFilter.Entry;
import javax.xml.ws.http.HTTPException;

import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;


public class SearchSolr {
	public static void main(String [] args) throws SolrServerException, HTTPException, IOException {

		//		String url = "http://129.241.111.168:8983/solr/#/";
		ArrayList<String> list = new ArrayList<String>();
		list.add("SanFrancisco");
		list.add("Mediaeval");
		list.add("London");
		list.add("POOLFutebal");
		list.add("Upcoming");
		list.add("WorldGEOUpcoming");
		//list.add("WorldTiles");

		HttpSolrServer server;
		String input= JOptionPane.showInputDialog("søkeord");
		String [] splitStrings = input.split(" ");
		String qyr;
		String qyr2;

		qyr = "attr_tag:" + splitStrings[0];
		qyr2 = "attr_tag:" + splitStrings[1];

		for (int i = 0; i < list.size(); i++) {
			server= new HttpSolrServer("http://129.241.111.168:8983/solr/"+list.get(i));

			SolrQuery query = new SolrQuery(qyr + " AND "+ qyr2);//Search for everything/anything
			QueryResponse qr = server.query(query); 
			SolrDocumentList result = qr.getResults();
			for (SolrDocument doc : result) {
				Object id = doc.get("url_s");
				ArrayList name = (ArrayList) doc.get("attr_tag");
				System.out.println(id + " " + name);
			}


		}














	}

}
