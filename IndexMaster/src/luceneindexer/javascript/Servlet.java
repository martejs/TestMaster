package luceneindexer.javascript;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import luceneindexer.search.SearchDBpedia;


public class Servlet extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String q = req.getParameter("q");
		PrintWriter out = resp.getWriter();

		out.println("<html>");
		out.println("<body>");
		out.println("The paramter q was \"" + q + "\".");
		out.println("</body>");
		out.println("</html>");

	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{

		String[] response = req.getParameterValues("method");

		String method = response[0];
		String field = response[1];

		SearchDBpedia searchDB = new SearchDBpedia(field, 3);

		PrintWriter out = resp.getWriter();

		out.println("<html>");
		out.println("<body>");
		out.println("You entered \"" + field + "\" into the text box.");
		out.println("</body>");
		out.println("</html>");
	}



}