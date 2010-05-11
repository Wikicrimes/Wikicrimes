package org.wikicrimes.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRpx extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3238008556008145747L;

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setHeader ("Pragma", "no-cache");
		response.setHeader ("Cache-Control", "no-cache");
		response.setDateHeader ("Expires", 0);
		System.out.println("opa!!");
		response.sendRedirect("http://www.wikicrimes.org");
		

	}
}
