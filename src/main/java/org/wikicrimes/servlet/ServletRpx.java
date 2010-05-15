package org.wikicrimes.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.UsuarioService;
import org.wikicrimes.util.rpx.Rpx;
import org.wikicrimes.web.FiltroForm;

public class ServletRpx extends HttpServlet {
    
	private static final long serialVersionUID = -5618832068937568479L;
	/**
     * The RPX base URL.
    */
    private static final String RPX_BASEURL = "https://wikicrimes2.rpxnow.com/";  
    /**
     * Your secret API code.
     */
    private static final String RPX_APIKEY = "156d45ece00ed923cad6daab2b717f9dbb5b5b82";
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // We had to use the token_url of this servlet to get back here, and
        // the magic token is in a parameter.
        String rpxToken = request.getParameter("token");
        Rpx rpx = new Rpx(RPX_APIKEY,RPX_BASEURL);
        // Make an auth dom element from the xml returned by the provider.
        Element rpxAuth = rpx.authInfo(rpxToken);
        // Check if authentication failed.
        String stat = rpxAuth.getAttribute("stat");
        if (!"ok".equals(stat)) {
            String error = "User authentication failed";
            response.sendError(HttpServletResponse.SC_FORBIDDEN, error);
            return;
        }
        // Generate a map of the profile attributes.
        Map<String, String> openIdMap = new HashMap<String, String>();
        Node profile = rpxAuth.getFirstChild();
        NodeList profileFields = profile.getChildNodes();
        for(int k = 0; k < profileFields.getLength(); k++) {
            Node n = profileFields.item(k);
            if (n.hasChildNodes()) {
                NodeList nFields = n.getChildNodes();
                for (int j = 0; j < nFields.getLength(); j++) {
                    Node nn = nFields.item(j);
                    String nodename = n.getNodeName();
                    if (!nn.getNodeName().startsWith("#"))
                        nodename += "." + nn.getNodeName();
                    openIdMap.put(nodename, nn.getTextContent());
                }
            } else
                openIdMap.put(n.getNodeName(), n.getTextContent());
        }        
     // checa se ja existe email cadastrado ou se e convidado
        ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());  
		UsuarioService usuarioService = (UsuarioService)springContext.getBean("usuarioService");
		String email = openIdMap.get("email");
		Usuario userResult = null;
		if(email!=null && !email.equals(""))
			userResult = usuarioService.getUsuario(email);
		else{
			email="";
        	Usuario userSearch = new Usuario();
        	userSearch.setExternalUrlRpx(openIdMap.get("identifier"));	
        	List<BaseObject> list = usuarioService.find(userSearch);
        	if(list != null && list.size() == 1){
        		userResult = (Usuario)list.get(0);
        	}
        	 
        }
		if(userResult == null){		
	        
	        HttpSession session = request.getSession();
	        FiltroForm filtroForm = (FiltroForm)session.getAttribute("filtroForm");
	        
	        Usuario usuario = new Usuario();
	        usuario.setPrimeiroNome(openIdMap.get("name.givenName"));
	        usuario.setUltimoNome(openIdMap.get("name.familyName"));
	        usuario.setSenha("");
	        usuario.setEmail(email);
	        usuario.setCidade("");
	        usuario.setIdiomaPreferencial("pt");
	        usuario.setPais(filtroForm.getPais());
	        usuario.setDataHoraRegistro(new Date());
	        usuario.setConfAutomatica(false);
	        if(email != null && !email.equals(""))
	        	usuario.setEmailAtivo("1");
	        usuario.setConfirmacao(Usuario.TRUE);
	        usuario.setLat(new Double(filtroForm.getLatMapa()));
	        usuario.setLng(new Double(filtroForm.getLngMapa()));
	        usuario.setReceberNewsletter("1");
	        usuario.setExternalUrlRpx(openIdMap.get("identifier"));
	        Perfil p = new Perfil();
	        p.setIdPerfil(new Long(Perfil.USUARIO));        
	        usuario.setPerfil(p);
	        
	        usuarioService.insert(usuario);
	        userResult = usuarioService.getUsuario(email);
		}
		HttpSession session = request.getSession();
		session.setAttribute("usuario", userResult);
        // Do something useful with them...
        response.sendRedirect(request.getRequestURL().toString().replace("/ServletRpx", ""));
    }
}

