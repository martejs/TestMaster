package luceneindexer.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import luceneindexer.search.SearchDBpedia;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Servlet extends AbstractHandler{

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setHeader("Access-Control-Allow-Origin", "mainPage.js");
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if(br != null){
			json = br.readLine();
		}

		// 2. initiate jackson mapper
		ObjectMapper mapper = new ObjectMapper();

		// 3. Convert received JSON to Article
		String string = mapper.readValue(json, String.class);
		String test = mapper.writeValueAsString(json);
		SearchDBpedia searchDB = new SearchDBpedia(test, 3);


		// 4. Set response type to JSON
		response.setContentType("application/json");            
		response.getWriter().write(json);	//just returns what has been given.


	}
	
	public static void main(String[] args) throws Exception
	{
		Server server = new Server(8080);
		server.setHandler(new Servlet());

		server.start();
		server.join();
	}



	

}
