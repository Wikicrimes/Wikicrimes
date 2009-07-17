package org.wikicrimes.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.TipoCrime;
import org.wikicrimes.model.TipoLocal;
import org.wikicrimes.service.CrimeService;

public class RSSGeneratorServlet extends HttpServlet {
	
	static String BASE_URL = "http://www.wikicrimes.org";
//	static String BASE_URL = "http://localhost:4343/wikicrimes";
	static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	private CrimeService crimeService;
	
	private static ApplicationContext ctx;
	private static SessionFactory sf;
	
	protected static void setUp() throws Exception {
		// the following is necessary for lazy loading
		sf = (SessionFactory) ctx.getBean("sessionFactory");
		// open and bind the session for this test thread.
		Session s = sf.openSession();
		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(s));
		// setup code here
	}
				 
	protected static void tearDown() throws Exception {
		// unbind and close the session.
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sf);
		Session s = holder.getSession();
		s.flush();
		TransactionSynchronizationManager.unbindResource(sf);
		SessionFactoryUtils.closeSession(s);
		// teardown code here
	}
	
	public void doGet(HttpServletRequest request,
            		  HttpServletResponse response)
		throws ServletException, IOException {
		ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		String lang = request.getRequestURL().toString();
		int index = lang.indexOf("/rss/")+5;
		if (index >= lang.length())
			lang = "";
		else
			lang = lang.substring(index);
		
		String locale = request.getParameter("loc");
		ResourceBundle bundle;
		if (locale == null || locale.equals("undefined")  || locale.equals(""))
			 bundle = ResourceBundle.getBundle("messages", request.getLocale());
		else 
			 bundle = ResourceBundle.getBundle("messages", new Locale(locale));
		Document document = null;
		try{	
			setUp();
			document = generateRss(lang, bundle);
			tearDown();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        
        response.addHeader("Content-Type", "text/xml");
        
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("ISO-8859-1");
		XMLWriter writer = new XMLWriter(response.getWriter(), format);
        writer.write( document );
	}
	
	private CrimeService getCrimeService() {
		if (this.crimeService == null) {
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			this.crimeService = (CrimeService)ctx.getBean("crimeService");
		}
		return this.crimeService;
	}
	
	private List getUltimosCrimes(int quantidade) {
		HashMap params = new HashMap();
		params.put("maxResults", quantidade);
		List crimes = this.getCrimeService().filter(params);
		return crimes;
	}
	
	private Document generateRss(String url, ResourceBundle bundle) {
		List crimes = this.getUltimosCrimes(15);
		
		Document document = DocumentHelper.createDocument();
        Element root = document.addElement( "rss" ).addAttribute("version", "2.0");
        
        Element channel = root.addElement( "channel" );
        channel.addElement("title").addText("Wikicrimes - "+bundle.getString("rss.titulo"));
        channel.addElement("link").addText(BASE_URL);
        Element img = channel.addElement("image");
        img.addElement("title").addText("Wikicrimes - "+bundle.getString("rss.titulo"));
        img.addElement("url").addText(BASE_URL+"/images/logo.jpg");
        
        int i = 1;
        for (Iterator it=crimes.iterator(); it.hasNext(); ++i) {
        	Crime crime = (Crime)it.next();
        	
        	StringBuffer descCrime = new StringBuffer();
        	descCrime.append(this.getTipoCrimeDesc(crime, bundle)).append(" ");
        	boolean temLocal = false;
        	if (crime.getCidade() != null) {
        		temLocal = true;
        		descCrime.append(bundle.getString("rss.str_in")).append(" ");
        		descCrime.append(crime.getCidade()).append(" ");
        	}
        	if (crime.getEstado() != null) {
        		if (!temLocal) 
        			descCrime.append(bundle.getString("rss.str_in")).append(" ");
        		temLocal = true;
        		descCrime.append(crime.getEstado()).append("-");
        	}
        	if (crime.getPais() != null) {
        		if (!temLocal) 
        			descCrime.append(bundle.getString("rss.str_in")).append(" ");
        		temLocal = true;
        		descCrime.append(crime.getPais()).append(" ");
        	}
        	descCrime.append("(").append(format.format(crime.getData())).append(") ");
        	
        	Element item = channel.addElement("item");
        	item.addElement("title").addText(descCrime.toString());
        	item.addElement("link").addText(BASE_URL+"/main.html?idcrime="+crime.getChave());
        	item.addElement("description").addText(crime.getDescricao());
        }
        
        return document;
	}
	
	private String getTipoCrimeDesc(Crime crime, ResourceBundle bundle) {
		StringBuffer desc = new StringBuffer();
		TipoCrime tc = crime.getTipoCrime();
		TipoLocal tl = crime.getTipoLocal();
		
		if (tc.getIdTipoCrime() != 5) {
			desc.append(bundle.getString(tc.getNome()));
			if (tl != null) {
				desc.append(" ").append(bundle.getString("rss.str_to")).append(" ");
				desc.append(bundle.getString(crime.getTipoVitima().getNome()));
			}
		}
		else if (tl != null) {
			desc.append(bundle.getString(crime.getTipoVitima().getNome()));
		}
		else {
			desc.append(bundle.getString("rss.crime"));
		}
		return desc.toString();
	}
}
