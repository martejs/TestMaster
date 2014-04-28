package luceneindexer.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD
import luceneindexer.search.SearchDBpedia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Servlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin","mainPage.js");
		
		// 1. get received JSON data from request
		System.out.println("Kalles fra javascript");
=======
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

>>>>>>> FETCH_HEAD
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if(br != null){
			json = br.readLine();
		}

		// 2. initiate jackson mapper
<<<<<<< HEAD
//		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();

		// 3. Convert received JSON to Article
//		String string = mapper.readValue(json, String.class);
		String test = mapper.writeValueAsString(json);
		SearchDBpedia searchDB = new SearchDBpedia(test);

//        String test1 = mapper.writeValueAsString(searchDB);

//		SearchDBpedia searchDB = new SearchDBpedia(string);
=======
		ObjectMapper mapper = new ObjectMapper();

		// 3. Convert received JSON to Article
		String string = mapper.readValue(json, String.class);
		String test = mapper.writeValueAsString(json);
		SearchDBpedia searchDB = new SearchDBpedia(test, 3);

>>>>>>> FETCH_HEAD

		// 4. Set response type to JSON
		response.setContentType("application/json");            
		response.getWriter().write(json);	//just returns what has been given.

<<<<<<< HEAD
		// 6. Send List<Article> as JSON to client
		//        mapper.writeValue(response.getOutputStream(), string);


	}
=======

	}
	
	public static void main(String[] args) throws Exception
	{
		Server server = new Server(8080);
		server.setHandler(new Servlet());

		server.start();
		server.join();
	}



	
>>>>>>> FETCH_HEAD

}
