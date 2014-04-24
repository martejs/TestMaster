package luceneindexer.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import luceneindexer.search.SearchDBpedia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Servlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin","mainPage.js");
		
		// 1. get received JSON data from request
		System.out.println("Kalles fra javascript");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if(br != null){
			json = br.readLine();
		}

		// 2. initiate jackson mapper
//		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();

		// 3. Convert received JSON to Article
//		String string = mapper.readValue(json, String.class);
		String test = mapper.writeValueAsString(json);
		SearchDBpedia searchDB = new SearchDBpedia(test);

        String test1 = mapper.writeValueAsString(searchDB);

//		SearchDBpedia searchDB = new SearchDBpedia(string);

		// 4. Set response type to JSON
		response.setContentType("application/json");            
		response.getWriter().write(json);	//just returns what has been given.

		// 6. Send List<Article> as JSON to client
		//        mapper.writeValue(response.getOutputStream(), string);


	}

}
