package org.wikicrimes.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wikicrimes.util.rpx.Rpx;
import org.wikicrimes.model.*;
import org.wikicrimes.web.*;

public class ServletRpx extends HttpServlet {
    
	private static final long serialVersionUID = -5618832068937568479L;
	/**
     * The RPX base URL.
    */
    private static final String RPX_BASEURL = "https://wikicrimes.rpxnow.com/";  
    /**
     * Your secret API code.
     */
    private static final String RPX_APIKEY = "28185ff566a3eaf1cbbd9395f4b1e422b564afb6";
 
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
        // Nested elements can be accessed with the full path:
        String name = openIdMap.get("name.formatted");
        String mail = openIdMap.get("email");
        HttpSession session = request.getSession();
        FiltroForm filtroForm = (FiltroForm)session.getAttribute("filtroForm");
        
        Usuario usuario = new Usuario();
        usuario.setPrimeiroNome(name);
        usuario.setEmail(mail);
        usuario.setCidade("");
//        usuario.setPais(pais);
        usuario.setConfAutomatica(false);
        Perfil p = new Perfil();
        p.setIdPerfil(new Long(Perfil.USUARIO));        
        usuario.setPerfil(p);
        // Do something useful with them...
        response.sendRedirect(request.getRequestURL().toString().replace("/ServletRpx", ""));
    }
}

