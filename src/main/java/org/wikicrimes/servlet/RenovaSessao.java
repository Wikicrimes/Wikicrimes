package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wikicrimes.model.Usuario;

public class RenovaSessao extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3238008556008145747L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		response.setHeader ("Pragma", "no-cache");
		response.setHeader ("Cache-Control", "no-cache");
		response.setDateHeader ("Expires", 0);

		PrintWriter writer = response.getWriter();
		Usuario u = (Usuario)request.getSession().getAttribute("usuario");
		if(u!=null)
			System.out.println("[" + new Date() + "] Sessão renovada para " + u.getEmail());
		PrintWriter saida = null;
		String linha = "Sessao renovada";  
		saida = new PrintWriter(writer, true);
		saida.print(linha);
		
		writer.close();

	}
}
