package org.wikicrimes.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Description: Classe auxiliar para manipulação de mensagens no contexto do Faces.
 * @author Fábio Barros
 */
public final class CookiesHelper {

    /**
	 * Construtor privado para impedir a instanciacao de um objeto. Forca
	 * utilizacao dos metodos na notacao estatica.
	 */
    private CookiesHelper() {
    }

    /**
     * Adiciona um cookie na resposta.
     * @param key Chave de identificacao do cookie
     * @param value Valor do cookie
     * @param age Tempo de vida do cookie
     */
    public static void addCookie(String key, String value, Integer age) {
    	// Criando o cookie
    	Cookie cookie = new Cookie(key, value);
    	if (age == null || age.equals(0)) {
    		cookie.setMaxAge(Integer.MAX_VALUE);
    	} else {
    		cookie.setMaxAge(age);
    	}
    	
    	// Armazenando o cookie
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		response.addCookie(cookie);
    }
    
    /**
     * Adiciona um cookie na resposta.
     * @param key Chave de identificacao do cookie
     * @return Cookie desejado
     */
    public static Cookie getCookie(String key) {
    	// Recuperando colecao de cookies
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
    	if (request.getCookies() != null) {
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(key)) {
				return cookie; 
			}
		}
    	}
		
		return null;
    }
}
