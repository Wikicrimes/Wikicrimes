package org.wikicrimes.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.wikicrimes.dao.UsuarioDao;
import org.wikicrimes.model.AreaObservacao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;

/**
 * 
 */
public class UsuarioDaoHibernate extends GenericCrudDaoHibernate implements
		UsuarioDao {

	public UsuarioDaoHibernate() {
		setEntity(Usuario.class);
	}
	
	public Usuario getByEmail(String email){
			Usuario usuario = new Usuario();
			usuario.setEmail(email);
		 List list= getHibernateTemplate().findByExample(usuario);
		 if (list.size()>0){
			 
			 return (Usuario) list.get(0);
		 }
		 else return null;
		 
		
	}

	public boolean exist(Usuario bo) {
		List list = getHibernateTemplate().findByExample(bo);
		
		if (list.size() > 0){
			return true;
		} else {
			return false;
		}
	}
	
public List<BaseObject> filter(final Map parameters) {
		
		
		
		return (List<BaseObject>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				
				String primeiroNome = (String)parameters.get("primeiroNome");
				String email = (String) parameters.get("email");
				String cidade = (String)parameters.get("cidade");
				String estado = (String)parameters.get("estado");
				Date dataInicial = (Date) parameters.get("dataInicial");
				Date dataFinal = (Date) parameters.get("dataFinal");
				String confirmacao = (String) parameters.get("confirmacao");
				String entCer = (String)parameters.get("entidadeCertificadora");
				
				Criteria criteria = session.createCriteria(getEntity());
				if ( primeiroNome != null && !primeiroNome.equalsIgnoreCase("")) {
					criteria.add(Expression.eq("primeiroNome", parameters.get("primeiroNome")));
				}
				if (email != null && !email.equalsIgnoreCase("")) {
					criteria.add(Expression.eq("email", parameters.get("email")));
				}
				if (cidade != null && !cidade.equalsIgnoreCase("")) {
					criteria.add(Expression.eq("cidade", parameters.get("cidade")));
				}
				if (estado != null && !estado.equalsIgnoreCase("")) {
					criteria.add(Expression.eq("estado", parameters.get("estado")));
				}
				if (dataInicial != null ) {
					criteria.add(Expression.ge("dataHoraRegistro", parameters.get("dataInicial")));
				}
				if (dataFinal != null ) {
					criteria.add(Expression.le("dataHoraRegistro", parameters.get("dataFinal")));
				}
				if(confirmacao != null && !confirmacao.equalsIgnoreCase("")){
					if(confirmacao.equalsIgnoreCase("1")){
						criteria.add(Expression.eq("confirmacao","1"));
					}						
					else{
						criteria.add(Expression.ne("confirmacao","1"));
					}
				}
				if(entCer != null && !entCer.equalsIgnoreCase("")&& !entCer.equalsIgnoreCase("-1")){
					if(entCer.equalsIgnoreCase("0")){
						criteria.add(Expression.isNotNull("entidadeCertificadora"));
					}
					else{
						criteria.add(Expression.eq("entidadeCertificadora.idEntidadeCertificadora",Long.parseLong((String)parameters.get("entidadeCertificadora"))));
					}	
				}
				
				criteria.addOrder(Order.asc("idUsuario"));
				
				return criteria.list();
			}
		});
	
		
	}
	
	public List<BaseObject> getUsuariosConfirmados() {

		String query = "from Usuario ";

		query += "where usu_confirmacao=1";

		return getHibernateTemplate().find(query);
	}

	

	public List<AreaObservacao> getAreas(Usuario u) {
		String query = "from AreaObservacao area ";

		query += "where area.usuario.idUsuario = "+u.getIdUsuario();

		return getHibernateTemplate().find(query);
	}

	@Override
	public Usuario getUsuarioKey(String key) {
		Usuario usuario = new Usuario();
		usuario.setMobileAppID(key);
		
		List list= getHibernateTemplate().findByExample(usuario);
		
		if (list!=null && !list.isEmpty()){
			Usuario user = (Usuario) list.get(0);
			user.setCountAtividadeMobile(user.getCountAtividadeMobile()+1);
			//getHibernateTemplate().update(user);
			return user;
		 }else{
			 return null;
		 }
		
	}
	

}
