package org.wikicrimes.web;

import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.Usuario;

public class GenericForm implements Form {
    
	public static final String FAILURE = "failure";
	public static final String SUCCESS = "success";
	public static final String SESSAO_EXPIRADA = "sessao_expirada";
	public static final String PAGINA_NAO_PERMITIDA = "pagina_nao_permitida";

	protected FacesContext facesContext;
	protected Map requestScope;
	protected Map sessionScope;
	protected Map applicationScope;
	protected Map requestParam;

	/**
	 * 
	 */
	
	
	public GenericForm() {
		facesContext = FacesContext.getCurrentInstance();
	   if (facesContext !=null) {
		requestScope =
			(Map) facesContext
				.getApplication()
				.createValueBinding("#{requestScope}")
				.getValue(facesContext);
		sessionScope =
			(Map) facesContext
				.getApplication()
				.createValueBinding("#{sessionScope}")
				.getValue(facesContext);
		applicationScope =
			(Map) facesContext
				.getApplication()
				.createValueBinding("#{applicationScope}")
				.getValue(facesContext);
		requestParam =
			(Map) facesContext
				.getApplication()
				.createValueBinding("#{param}")
				.getValue(facesContext);
		}
	}
	
	public HttpSession getExternalSession() {
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = 
			(HttpSession) ex.getSession(true);
		
		return session;
	}

	public Map getApplicationScope() {
		return applicationScope;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public Map getRequestParam() {
		return requestParam;
	}

	public Map getRequestScope() {
		return requestScope;
	}

	public Map getSessionScope() {
		return sessionScope;
	}

	/**
	 * @param key
	 * @param arg
	 */
	public void addMessage(String key, String arg[]) {
		// sure is a lot of work to get the named ResourceBundle in JSF, eh?
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle messages = ResourceBundle.getBundle(bundleName);

		// it's even more work to format a message with args
		MessageFormat form = new MessageFormat(messages.getString(key));
		
		
		String msg = form.format(arg);

		// add message to session so it can live past redirects
		// the MessageFilter class will take care of removing it
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("message", msg);
	}	
	
	public void clearMessages() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.removeAttribute("message");
	}
	
	public void addMessageFaces(String key,String param,String formID){
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
		.getFactory(FactoryFinder.APPLICATION_FACTORY);
		
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle messages = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());

		MessageFormat form = new MessageFormat(messages.getString(key));
		String msg = form.format(new Object[] { param });
		
		//System.out.println(msg); OK - gera a mensagem
		
		FacesMessage fm = new FacesMessage();
		fm.setSeverity(FacesMessage.SEVERITY_ERROR);
		fm.setDetail(msg);//seta a msg
		FacesContext.getCurrentInstance().addMessage(formID, fm);
	}
		
	
	public void addMessage(String key, String arg) {
		// sure is a lot of work to get the named ResourceBundle in JSF, eh?
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle messages = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());

		// it's even more work to format a message with args
		MessageFormat form = new MessageFormat(messages.getString(key));

		String msg = form.format(new Object[] { arg });

		// add message to session so it can live past redirects
		// the MessageFilter class will take care of removing it
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("message", msg);
	}	
//	 Convenience methods ====================================================
	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}
	
	public String getMessage(String key, String arg) {
		// sure is a lot of work to get the named ResourceBundle in JSF, eh?
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		String bundleName = factory.getApplication().getMessageBundle();
		ResourceBundle messages = ResourceBundle.getBundle(bundleName,FacesContext.getCurrentInstance().getViewRoot().getLocale());

		// it's even more work to format a message with args
		MessageFormat form = new MessageFormat(messages.getString(key));

		String msg = form.format(new Object[] { arg });
		return msg;
	}
	
	public boolean expirouSessao() {

		Usuario usuario = (Usuario) this.getSessionScope().get("usuario");
		if (usuario == null)
			return true;
		else
			return false;
	}

	public boolean isAdmin() {

		Usuario usuario = (Usuario) this.getSessionScope().get("usuario");

		if (usuario.getPerfil().getIdPerfil() == Perfil.ADMINISTRADOR)
			return true;
		else
			return false;
	}
}
