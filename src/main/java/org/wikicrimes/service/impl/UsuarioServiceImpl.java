package org.wikicrimes.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.hibernate.Hibernate;
import org.springframework.core.io.Resource;
import org.wikicrimes.dao.ReputacaoDao;
import org.wikicrimes.dao.UsuarioDao;
import org.wikicrimes.model.AreaObservacao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.PontosArea;
import org.wikicrimes.model.Reputacao;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.UsuarioRedeSocial;
import org.wikicrimes.service.EmailService;
import org.wikicrimes.service.UsuarioService;

public class UsuarioServiceImpl extends GenericCrudServiceImpl implements UsuarioService {

	private Resource txtTermoUso;
	private Resource txtTermoUso_EN;
	private Resource txtTermoUso_ES;
	private UsuarioDao usuarioDao;
	private EmailService emailService;
	private ReputacaoDao reputacaoDao;

	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	/*
	 * private void sendMailConfirmation(Usuario usuario){ SimpleMailMessage msg = new
	 * SimpleMailMessage(this.templateMessage); msg.setTo(usuario.getEmail()); msg.setText("Voce foi
	 * cadastrado no WikiCrimes.org. <br> Clique <a href=>aqui</a> para confirmar seu
	 * cadastro..."); msg.setFrom("admin@wikicrimes.org");
	 * msg.setSubject("WikiCrimes.org - Confirmação de Cadastro "); try { this.mailSender.send(msg); } catch
	 * (MailException ex) { // simply log it and go on... System.err.println(ex.getMessage()); } }
	 */
	@Override
	public boolean insert(BaseObject u)
	{
		boolean result = false;
		Usuario usuario = ((Usuario) u);

		if (!exist(usuario)) 
		{
			if (usuario.getPerfil().getIdPerfil() != Perfil.CONVIDADO) 
			{
				if(usuario.getExternalUrlRpx() == null)
					usuario.setConfirmacao(Usuario.FALSE);
				result = super.insert(usuario);
				usuario.setChaveConfirmacao(String.valueOf(usuario.hashCode()));
				if(usuario.getExternalUrlRpx() == null)	
					if (usuario.getIdiomaPreferencial() == null)
						emailService.sendMailConfirmation(usuario, FacesContext.getCurrentInstance()
								.getViewRoot().getLocale().toString());
					else
						emailService.sendMailConfirmation(usuario, usuario.getIdiomaPreferencial());
			}
			else 
			{
				// List list =this.find(usuario);
				Usuario usuarioGuest = this.getUsuario(usuario.getEmail());
				usuarioGuest.setAniversario(usuario.getAniversario());
				usuarioGuest.setChaveConfirmacao(String.valueOf(usuario.hashCode()));
				usuarioGuest.setCidade(usuario.getCidade());
				usuarioGuest.setConfirmacao(Usuario.FALSE);
				usuarioGuest.setEstado(usuario.getEstado());
				usuarioGuest.setHomepage(usuario.getHomepage());
				usuarioGuest.setIp(usuario.getIp());
				usuarioGuest.setLat(usuario.getLat());
				usuarioGuest.setLng(usuario.getLng());
				usuarioGuest.setPais(usuario.getPais());
				usuarioGuest.setPerfil(new Perfil(Perfil.USUARIO));
				usuarioGuest.setPrimeiroNome(usuario.getPrimeiroNome());
				usuarioGuest.setSenha(usuario.getSenha());
				usuarioGuest.setSexo(usuario.getSexo());
				usuarioGuest.setUltimoNome(usuario.getUltimoNome());
				usuarioGuest.setDataHoraRegistro(usuario.getDataHoraRegistro());

				result = this.update(usuarioGuest);
				if(usuario.getExternalUrlRpx()==null)	
					emailService.sendMailConfirmation(usuarioGuest, FacesContext.getCurrentInstance()
						.getViewRoot().getLocale().toString());
			}

			criarReputacao(usuario);
		}
		
		return result;
	}
	
	@Override
	public List<BaseObject> find(Usuario u){
		List<BaseObject> usuarios = super.find(u);
		for (BaseObject usuario : usuarios) {
			Usuario u2 = (Usuario) usuario;
			Hibernate.initialize(u2.getPerfil());
		}
		return usuarios;
	}

	private void criarReputacao(Usuario usuario) 
	{
		Reputacao reputacao = new Reputacao(null, 0.5, new Date(), usuario);

		if (usuario.getEntidadeCertificadora() != null)
			reputacao.setReputacao(1.0);

		Set<Reputacao> reputacoes = usuario.getReputacoes();
		
		if (reputacoes == null)
			reputacoes = new HashSet<Reputacao>();
		
		reputacoes.add(reputacao);
		usuario.setReputacoes(reputacoes); // caso null
		reputacaoDao.save(reputacao);
	}

	public boolean exist(BaseObject bo) {
		return getDao().exist(bo);
	}

	public Usuario login(Usuario u) {
		List list = getDao().find(u);

		if (list.size() > 0) {

			Usuario u2 = (Usuario) list.get(0);
			Hibernate.initialize(u2.getPerfil());
			return u2;

		}

		return null;
	}

	public Resource getTxtTermoUso() {
		return txtTermoUso;
	}

	public void setTxtTermoUso(Resource txtTermoUso) {
		this.txtTermoUso = txtTermoUso;
	}

	public Resource getTxtTermoUso_EN() {
		return txtTermoUso_EN;
	}

	public void setTxtTermoUso_EN(Resource txtTermoUso) {
		this.txtTermoUso_EN = txtTermoUso;
	}

	public Resource getTxtTermoUso_ES() {
		return txtTermoUso_ES;
	}

	public void setTxtTermoUso_ES(Resource txtTermoUso) {
		this.txtTermoUso_ES = txtTermoUso;
	}

	/*
	 * Metodo que cria um usuario convidado. Leo Ayres \(non-Javadoc)
	 * @see org.wikicrimes.service.UsuarioService#getUsuarioConfirmacao(java.lang.String)
	 */
	public Usuario retornaUsuarioConfirmacao(String email, String idioma) {
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha("guest");
		usuario.setIdiomaPreferencial(idioma);
		usuario.setPerfil(new Perfil(Perfil.CONVIDADO));
		usuario.setConfAutomatica(false);

		this.getDao().save(usuario);
		criarReputacao(usuario);
		
		return usuario;
	}

	public boolean alterarUsuario(Usuario usuario) {
		if (usuarioDao.save(usuario)) {
			return true;
		}
		else {
			return false;
		}

	}

	/*
	 * Metodo que retorna usuario a partir do email. Retorna null se nao existir Leo Ayres
	 */
	public Usuario getUsuario(String email) {
		Usuario u = (Usuario) ((UsuarioDao) this.getDao()).getByEmail(email); 
		if(u != null)
			Hibernate.initialize(u.getPerfil());
		return u;

	}

	public void cadastrarAreaObservacao(AreaObservacao area) {
		super.insert(area);
	}

	public void excluirAreaObservacao(AreaObservacao area) {
		Set<PontosArea> pontos = area.getPontos();
		for (Iterator iterator = pontos.iterator(); iterator.hasNext();) {
			PontosArea ponto = (PontosArea) iterator.next();
			super.delete(ponto);
		}
		super.delete(area);

	}

	public List<BaseObject> filter(Map parameters) {
		return usuarioDao.filter(parameters);
	}

	public List<BaseObject> UsuariosConf() {
		return usuarioDao.getUsuariosConfirmados();
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public void salvar(UsuarioRedeSocial urs) {
		getUsuarioDao().save(urs);
	}

	public List<AreaObservacao> getAreas(Usuario u) {
		// TODO Auto-generated method stub
		return usuarioDao.getAreas(u);
	}

	@Override
	public Usuario getUsuarioKey(String key) {
		return usuarioDao.getUsuarioKey(key);
	}

	// Reputacao
	public ReputacaoDao getReputacaoDao() {
		return reputacaoDao;
	}
	public void setReputacaoDao(ReputacaoDao reputacaoDao) {
		this.reputacaoDao = reputacaoDao;
	}
	
}
