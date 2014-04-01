package luceneindexer.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import luceneindexer.search.SearchDBpedia;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Servlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin","Servlet.java");
		
		// 1. get received JSON data from request
		System.out.println("Kalles fra javascript");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if(br != null){
			json = br.readLine();
		}

		// 2. initiate jackson mapper
		ObjectMapper mapper = new ObjectMapper();

		// 3. Convert received JSON to Article
		String string = mapper.readValue(json, String.class);
		SearchDBpedia searchDB = new SearchDBpedia(string);

		// 4. Set response type to JSON
		response.setContentType("application/json");            


		// 6. Send List<Article> as JSON to client
		//        mapper.writeValue(response.getOutputStream(), string);


	}

}
