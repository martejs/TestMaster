package luceneindexer.javascript;

import java.io.IOException;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import luceneindexer.search.SearchDBpedia;


public class Servlet extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String q = req.getParameter("q");
		System.out.println("Dette er q"+q);

//		SearchDBpedia searchDB = new SearchDBpedia(q, 3);


	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		String field = req.getParameter("field");
		PrintWriter out = resp.getWriter();

		out.println("<html>");
		out.println("<body>");
		out.println("You entered \"" + field + "\" into the text box.");
		out.println("</body>");
		out.println("</html>");
	}



}