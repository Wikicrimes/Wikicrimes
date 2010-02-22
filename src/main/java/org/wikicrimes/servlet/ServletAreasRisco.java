package org.wikicrimes.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.AreaRisco;
import org.wikicrimes.model.PontoLatLng;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.AreaRiscoService;

/**
 * Trata requisições para persistir e recuperar poligionos de "Areas Risco"
 * 
 * @author victor
 */
public class ServletAreasRisco extends HttpServlet {

	private AreaRiscoService service;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String acao = req.getParameter("acao");
		if(acao != null){

			AreaRiscoService service = getService();
			HttpSession sessao = req.getSession();
			PrintWriter out = resp.getWriter();
			Usuario usuario = (Usuario)sessao.getAttribute("usuario");
			String idStr = req.getParameter("id");
			String nome = req.getParameter("nome");
			String verticeStr = req.getParameter("vertices");
			
			if(acao.equals("salvar")){
				AreaRisco area = new AreaRisco();
				area.setUsuario(usuario);
				area.setDataHoraRegistro(new Date());
				area.setNome(nome);
				List<PontoLatLng> vertices = area.setVerticesAndReturn(verticeStr); 
				service.save(vertices); //salva os pontos e tb coloca os pontos na area
				service.save(area);
				out.write(""+area.getIdAreaRisco());
			}else if(acao.equals("listar")){
				if(usuario != null){
					List<AreaRisco> areas = service.listAreas(usuario);
					out.print(AreaRisco.listToString(areas));
				}
			}else if(acao.equals("renomear")){
				AreaRisco area = (AreaRisco)service.get(Long.valueOf(idStr));
				area.setNome(nome);
				service.update(area);
			}else if(acao.equals("alterarFormato")){
				AreaRisco area = (AreaRisco)service.get(Long.valueOf(idStr));
				List<PontoLatLng> vertices = area.setVerticesAndReturn(verticeStr);
				service.save(vertices);
				service.update(area);
			}else if(acao.equals("excluir")){
				AreaRisco area = new AreaRisco();
				area.setIdAreaRisco(Long.valueOf(idStr));
				service.delete(area);
			}
			out.close();
		}
	}
	
	private AreaRiscoService getService(){
		if(service == null){
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			service = (AreaRiscoService)springContext.getBean("areaRiscoService");
		}
		return service;
	}
	
}
