package org.wikicrimes.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
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

		
		
		String mobile = request.getParameter("mobile"); // Verifica se a requisicao e mobile
		String mobileLat = request.getParameter("mobileLat"); // Verifica se a requisicao e mobile
		String mobileLng = request.getParameter("mobileLng"); // Verifica se a requisicao e mobile

		String rpxToken = request.getParameter("token");

		Rpx rpx = new Rpx(RPX_APIKEY, RPX_BASEURL);
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
		for (int k = 0; k < profileFields.getLength(); k++) {
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
		// System.out.println(openIdMap);

		// checa se ja existe email cadastrado ou se e convidado
		ApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		UsuarioService usuarioService = (UsuarioService) springContext
				.getBean("usuarioService");

		String email = openIdMap.get("email");

		String externalToken = md5(new Date() + openIdMap.get("identifier")); // Gera o external token para o acesso mobile

		Usuario userResult = null;
		if (email != null && !email.equals(""))
			userResult = usuarioService.getUsuario(email);
		else {
			email = "";
			Usuario userSearch = new Usuario();
			userSearch.setExternalUrlRpx(openIdMap.get("identifier"));
			List<BaseObject> list = usuarioService.find(userSearch);
			if (list != null && list.size() == 1) {
				userResult = (Usuario) list.get(0);
			}

		}

		if (userResult == null && email != null && !email.equals("")) {

			HttpSession session = request.getSession();
			FiltroForm filtroForm = (FiltroForm) session
					.getAttribute("filtroForm");

			Usuario usuario = new Usuario();
			usuario.setPrimeiroNome(openIdMap.get("name.givenName") == null ? ""
					: openIdMap.get("name.givenName"));
			usuario.setUltimoNome(openIdMap.get("name.familyName") == null ? ""
					: openIdMap.get("name.familyName"));
			usuario.setSenha("");
			usuario.setEmail(email);
			usuario.setCidade("");
			usuario.setIdiomaPreferencial(request.getLocale().getLanguage());

			usuario.setPais(getCountry(mobile, mobileLat, mobileLng, filtroForm)); // Se for mobile usa o pais do celular

			usuario.setDataHoraRegistro(new Date());
			usuario.setConfAutomatica(false);
			if (email != null && !email.equals(""))
				usuario.setEmailAtivo("1");
			usuario.setConfirmacao(Usuario.TRUE);
			usuario.setLat(mobile == null ? new Double(filtroForm.getLatMapa())
					: new Double(Double.parseDouble(mobileLat))); // Se for mobile usa o lat do celular
			usuario.setLng(mobile == null ? new Double(filtroForm.getLngMapa())
					: new Double(Double.parseDouble(mobileLng))); // Se for mobile usa o lon do celular
			usuario.setReceberNewsletter("1");
			usuario.setExternalUrlRpx(openIdMap.get("identifier"));
			usuario.setExternalToken(mobile == null ?null:externalToken);
			Perfil p = new Perfil();
			p.setIdPerfil(new Long(Perfil.USUARIO));
			usuario.setPerfil(p);

			usuarioService.insert(usuario);
			userResult = usuarioService.getUsuario(email);
		}

		HttpSession session = request.getSession();
		if (userResult != null) {
			System.out.println("[" + new Date() + "] " + email
					+ " efetuou o login pelo RPX...");
			userResult.setExternalToken(mobile == null ?null:externalToken);
			usuarioService.alterarUsuario(userResult);
			session.setAttribute("usuario", userResult);

		} else {
			
			session.setAttribute("rpx_usuario_sem_email", "sim");
			session.setAttribute("rpx_provider", openIdMap.get("providerName"));

		}

		
		if (mobile == null) {
			response.sendRedirect(request.getRequestURL().toString()
					.replace("/ServletRpx", ""));
		} else {
			response.getWriter().print(externalToken);
		}

	}

	public static String md5(String senha) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
		sen = hash.toString(16);
		return sen;
	}

	public String getCountry(String mobile, String mobileLat, String mobileLng, FiltroForm filtroForm) {
		if (mobile == null) {
			return filtroForm.getPais();
		} else {
			URL googleAPI;
			String country = "BR";
			try {
				googleAPI = new URL(
						"http://maps.googleapis.com/maps/api/geocode/json?latlng="+mobileLat+","+mobileLng+"&sensor=false");

				URLConnection yc = googleAPI.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						yc.getInputStream()));
				String inputLine;
				StringBuffer outputString = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					outputString.append(inputLine);
				}

				in.close();

				JSONObject object = (JSONObject) new JSONTokener(
						outputString.toString()).nextValue();
				String status = object.getString("status");

				if (status.equalsIgnoreCase("OK")) {
					JSONArray results = object.getJSONArray("results");
					JSONObject firstResult = results.getJSONObject(0);
					JSONArray addressComponents = firstResult
							.getJSONArray("address_components");

					for (int x = 0; x < addressComponents.length(); x++) {
						JSONObject components = addressComponents
								.getJSONObject(x);
						JSONArray types = components.getJSONArray("types");
						if (types.getString(0).equalsIgnoreCase("country")) {
							country = components.getString("short_name");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return country;
		}
	}
}
