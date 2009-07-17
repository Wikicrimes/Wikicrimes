package org.wikicrimes.dao.hibernate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.wikicrimes.dao.UsuarioDao;
import org.wikicrimes.model.BaseObject;
import org.wikicrimes.model.Usuario;


@Component("usuarioDaoHibernate")
public class UsuarioDaoImpl extends HibernateDaoGenerico<Usuario> implements
UsuarioDao {

	@Override
	protected Class<Usuario> getClasseEntidade() {
		// TODO Auto-generated method stub
		return null;
	}


	public List<BaseObject> autenticar(Usuario usuario) {		
		String query = "from Usuario u "+
		"where u.email like '"+usuario.getEmail()+"' " +
		"and u.senha like '"+usuario.getSenha()+"' " +
		"and u.perfil.nome like 'ADMINISTRADOR'";
		return getHibernateTemplate().find(query);
			

	}

	public List<BaseObject> filter(final Map parameters) {					

		String primeiroNome = (String)parameters.get("primeiroNome");
		String email = (String) parameters.get("email");
		String cidade = (String)parameters.get("cidade");
		String estado = (String)parameters.get("estado");
		String idioma = (String)parameters.get("idioma");
		Date dataInicial = (Date) parameters.get("dataInicial");
		Date dataFinal = (Date) parameters.get("dataFinal");
		String confirmacao = (String) parameters.get("confirmacao");
		String entCer = (String)parameters.get("entidadeCertificadora");
		String qtdCrimes = ""+parameters.get("qtdCrimes");
		String operadores = ""+parameters.get("operadores");

		String query;
		if(entCer != null && !entCer.equalsIgnoreCase("")&& !entCer.equalsIgnoreCase("-1")){
			query = "select u.entidadeCertificadora as entidadeCertificadora, ";
		}
		else{
			query = "select ";
		}

		query+="u.idUsuario as idUsuario, u.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, u.email as email, u.cidade as cidade," +
		" u.estado as estado, u.dataHoraRegistro as dataHoraRegistro, u.idiomaPreferencial as idiomaPreferencial, " +
		" (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
		" (select count(*) from AreaObservacao a where a.usuario.idUsuario = u.idUsuario) as qtdAreas " +
		" from Usuario u where ";
		
		boolean entrouNocriterio = false;
		
		if ( primeiroNome != null && !primeiroNome.equalsIgnoreCase("")){
			query +="u.primeiroNome like '%"+primeiroNome+"%' ";
			entrouNocriterio = true;
		}
		if (cidade != null && !cidade.equalsIgnoreCase("")){ 
			query +="and u.cidade like '%"+cidade+"%' ";
			entrouNocriterio = true;
		}
		if (estado != null && !estado.equalsIgnoreCase("-1")){
			query+="and u.estado like '"+estado+"' " ;
			entrouNocriterio = true;
		}
		if (idioma != null && !idioma.equalsIgnoreCase("-1")){
			query+="and u.idiomaPreferencial like '"+idioma+"' " ;
			entrouNocriterio = true;
		}
		if(qtdCrimes!=null && operadores!=null){
			if(operadores.equalsIgnoreCase("2")){
				query+="and (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) >= '"+ qtdCrimes+"' ";
				entrouNocriterio = true;
			}else if(operadores.equalsIgnoreCase("4")){
				query+="and (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) = '"+ qtdCrimes+"' ";
				entrouNocriterio = true;
			}else if(operadores.equalsIgnoreCase("3")){
				query+="and (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) <= '"+ qtdCrimes+"' ";
				entrouNocriterio = true;
			}else if(operadores.equalsIgnoreCase("1")){
				query+="and (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) < '"+ qtdCrimes+"' ";
				entrouNocriterio = true;
			}else if(operadores.equalsIgnoreCase("0")){
				query+="and (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) > '"+ qtdCrimes+"' ";
				entrouNocriterio = true;
			}
		}
		if(confirmacao != null && !confirmacao.equalsIgnoreCase("-1")){
			if(confirmacao.equalsIgnoreCase("1")){
				query+="and u.confirmacao like '1' "; 
				entrouNocriterio = true;
			}						
			else if(confirmacao.equalsIgnoreCase("0")){
				query+="and u.confirmacao like '0' ";
				entrouNocriterio = true;
			}
			else 
				query+="and u.perfil.idPerfil like '2' ";
				entrouNocriterio = true;
		}
		if(entCer != null && !entCer.equalsIgnoreCase("")&& !entCer.equalsIgnoreCase("-1")){
			if(entCer.equalsIgnoreCase("0")){
				query+="and u.entidadeCertificadora is not null  ";
				entrouNocriterio = true;
			}
			else{
				query+="and u.entidadeCertificadora.idEntidadeCertificadora = "+Long.parseLong(entCer)+" ";
				entrouNocriterio = true;
			}	
		}
		if (dataInicial != null ){
			Timestamp dtInicial = new Timestamp(dataInicial.getTime());
			query+="and u.dataHoraRegistro >= '"+ dtInicial+"' ";
			entrouNocriterio = true;
		}
		if (dataFinal != null ){
			Timestamp dtFinal = new Timestamp(dataFinal.getTime());
			query+="and u.dataHoraRegistro <= '"+ dtFinal+"' ";
			entrouNocriterio = true;
		}
		if (email != null && !email.equalsIgnoreCase("")){	
			query+="and u.email like '%"+email+"%'";
			entrouNocriterio = true;
		}
		if(!entrouNocriterio){
			query=query.replaceAll("from Usuario u where ", "from Usuario u ");
		}
		query=query.replaceAll("where and", "where");
		//System.out.println(query);
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery(query)
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();


//		Criteria criteria = getHibernateTemplate().getSessionFactory().openSession().createCriteria(getClasseEntidade());
//		if ( primeiroNome != null && !primeiroNome.equalsIgnoreCase("")) {
//		criteria.add(Expression.eq("primeiroNome", parameters.get("primeiroNome")));
//		}
//		if (email != null && !email.equalsIgnoreCase("")) {
//		criteria.add(Expression.eq("email", parameters.get("email")));
//		}
//		if (cidade != null && !cidade.equalsIgnoreCase("")) {
//		criteria.add(Expression.eq("cidade", parameters.get("cidade")));
//		}
//		if (estado != null && !estado.equalsIgnoreCase("")) {
//		criteria.add(Expression.eq("estado", parameters.get("estado")));
//		}
//		if (dataInicial != null ) {
//		criteria.add(Expression.ge("dataHoraRegistro", parameters.get("dataInicial")));
//		}
//		if (dataFinal != null ) {
//		criteria.add(Expression.le("dataHoraRegistro", parameters.get("dataFinal")));
//		}
//		if(confirmacao != null && !confirmacao.equalsIgnoreCase("")){
//		if(confirmacao.equalsIgnoreCase("1")){
//		criteria.add(Expression.eq("confirmacao","1"));
//		}						
//		else{
//		criteria.add(Expression.ne("confirmacao","1"));
//		}
//		}
//		if(entCer != null && !entCer.equalsIgnoreCase("")&& !entCer.equalsIgnoreCase("-1")){
//		if(entCer.equalsIgnoreCase("0")){
//		criteria.add(Expression.isNotNull("entidadeCertificadora"));
//		}
//		else{
//		criteria.add(Expression.eq("entidadeCertificadora.idEntidadeCertificadora",Long.parseLong((String)parameters.get("entidadeCertificadora"))));
//		}	
//		}

//		criteria.addOrder(Order.asc("idUsuario"));

//		return criteria.list();
	}	

	public void update(Usuario usuario){
		super.atualizarEntidade(usuario);
	}

	public List<BaseObject> GetUserById(String id){
		String query = "from Usuario u "+
		"where u.idUsuario like '"+id+"'";
		return getHibernateTemplate().find(query);
	}

	public List<BaseObject> getTotalUsuarios(){
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery("select u.idUsuario as idUsuario, u.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, u.email as email, u.cidade as cidade," +
				" u.estado as estado, u.dataHoraRegistro as dataHoraRegistro, u.idiomaPreferencial as idiomaPreferencial, " +
				" (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
				" (select count(*) from AreaObservacao a where a.usuario.idUsuario = u.idUsuario) as qtdAreas " +
		" from Usuario u")
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();
	}
	public Integer countTotalUsuarios(){
		String query = "select count(*) from Usuario u ";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

	public List<BaseObject> getUsuariosConf(){
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery("select u.idUsuario as idUsuario, u.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, u.email as email, u.cidade as cidade," +
				" u.estado as estado, u.dataHoraRegistro as dataHoraRegistro, u.idiomaPreferencial as idiomaPreferencial, " +
				" (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
				" (select count(*) from AreaObservacao a where a.usuario.idUsuario = u.idUsuario) as qtdAreas " +
		" from Usuario u where u.confirmacao like '1'")
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();
	}
	public Integer countUsuariosConf(){
		String query = "select count(*) from Usuario u "+
		"where u.confirmacao like '1'";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

	public List<BaseObject> getUsuariosNConf(){
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery("select u.idUsuario as idUsuario, u.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, u.email as email, u.cidade as cidade," +
				" u.estado as estado, u.dataHoraRegistro as dataHoraRegistro, u.idiomaPreferencial as idiomaPreferencial, " +
				" (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
				" (select count(*) from AreaObservacao a where a.usuario.idUsuario = u.idUsuario) as qtdAreas " +
		" from Usuario u where u.confirmacao like '0'")
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();
	}
	public Integer countUsuariosNConf(){
		String query = "select count(*) from Usuario u "+
		"where u.confirmacao like '0'";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

	public List<BaseObject> getUsuariosConv(){
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery("select u.idUsuario as idUsuario, u.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, u.email as email, u.cidade as cidade," +
				" u.estado as estado, u.dataHoraRegistro as dataHoraRegistro, u.idiomaPreferencial as idiomaPreferencial, " +
				" (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
				" (select count(*) from AreaObservacao a where a.usuario.idUsuario = u.idUsuario) as qtdAreas " +
		" from Usuario u where u.perfil.idPerfil = '2'")
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();
	}
	public Integer countUsuariosConv(){
		String query = "select count(*) from Usuario u "+
		"where u.perfil.idPerfil = '2'";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

	public List<BaseObject> getUsuariosAdm(){
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery("select u.idUsuario as idUsuario, u.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, u.email as email, u.cidade as cidade," +
				" u.estado as estado, u.dataHoraRegistro as dataHoraRegistro, u.idiomaPreferencial as idiomaPreferencial, " +
				" (select count(*) from Crime c where u.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
				" (select count(*) from AreaObservacao a where a.usuario.idUsuario = u.idUsuario) as qtdAreas " +
		" from Usuario u where u.perfil.idPerfil = '3'")
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();

	}
	public Integer countUsuariosAdmin(){
		String query = "select count(*) from Usuario u "+
		"where u.perfil.idPerfil = '3'";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

	public List<BaseObject> getUsuariosArea(){
		//SELECT count(distinct a.usu_idusuario) FROM `wikicrimes`.`tb_aro_area_observacao` a;
		String query = "select distinct a.usuario from AreaObservacao a";
		return getHibernateTemplate().getSessionFactory().openSession().
		createQuery("select distinct a.usuario.idUsuario as idUsuario, a.usuario.primeiroNome as primeiroNome, u.ultimoNome as ultimoNome, " +
				"a.usuario.email as email, a.usuario.cidade as cidade, a.usuario.estado as estado, " +
				"a.usuario.dataHoraRegistro as dataHoraRegistro, a.usuario.idiomaPreferencial as idiomaPreferencial, " +
				" (select count(*) from Crime c where a.usuario.idUsuario = c.usuario.idUsuario) as qtdCrimes, " +
				" (select count(*) from AreaObservacao a1 where a1.usuario.idUsuario = a.usuario.idUsuario) as qtdAreas " +
		" from AreaObservacao a")
		.setResultTransformer(Transformers.aliasToBean(Usuario.class)).list();

	}
	public Integer countUsuariosArea(){
		String query = "select count(distinct a.usuario.idUsuario) from AreaObservacao a";
		Long cont = (Long) getHibernateTemplate().find(query).iterator().next();

		return cont.intValue();
	}

}
