package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.wikicrimes.model.BaseObject;

/**
 * Implementa métodos genéricos de persistência.
 * 
 * @param <T>
 *            Tipo de entidade a qual a persistência será realizada.
 */
public abstract class HibernateDaoGenerico<T extends BaseObject> extends HibernateDaoSupport {

	/**
	 * Variável para o warning unchecked.
	 */
	protected static final String UNCHECKED = "unchecked";

	/**
	 * Usado para injeção do Session Factory com annotations.
	 * 
	 * @param sessionFactory
	 *            Session Factory do hibernate
	 */
	@Autowired
	public final void setHibernateSessionFactory(
			@Qualifier("hibernateSessionFactory")
			SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	/**
	 * Utilizado pelos métodos de persistência para a identificação de qual
	 * classe a persistência está sendo feita. Utilizada na implementação do
	 * padrão Template Method.
	 * 
	 * @return a classe da entidade a qual o DAO realiza a persistência.
	 */
	protected abstract Class<T> getClasseEntidade();

	/**
	 * Insere a entidade na base de dados.
	 * 
	 * @param entidade
	 *            Entidade a ser persistida.
	 */
	protected void inserirEntidade(T entidade) {
		getHibernateTemplate().saveOrUpdate(entidade);
	}

	/**
	 * Exclui a entidade na base de dados.
	 * 
	 * @param entidade
	 *            Entidade a ser persistida.
	 */
	protected void excluirEntidade(T entidade) {
		getHibernateTemplate().delete(entidade);
	}
	
	/**
	 * Atualiza a entidade na base de dados.
	 * 
	 * @param entidade
	 *            entidade a ser atualizada
	 */
	protected void atualizarEntidade(T entidade) {
		getHibernateTemplate().merge(entidade);
	}

	/**
	 * Salva a entidade na base de dados.
	 * 
	 * @param entidade
	 *            Entidade a ser persistida.
	 */
	protected void salvarEntidade(T entidade) {
		/*
		getHibernateTemplate().saveOrUpdate(entidade);
		 */
		
			inserirEntidade(entidade);
		
	}
	
	/**
	 * Conecta o objeto a sessão ativa do hibernate.
	 * 
	 * @param entidade
	 *            entidade a ser conecatada/reconecatada
	 */
	protected void conectarEntidade(T entidade) {
		getHibernateTemplate().lock(entidade, LockMode.NONE);
	}

	/**
	 * Seleciona todos os registros aramzenado de um entidade {@link T}
	 * ordenados, ou não, pelos campos passados.
	 * 
	 * @param camposOrdenacao
	 *            campos usados para ordenar a consulta (opcional)
	 * @return lista de {@link T}
	 */
	@SuppressWarnings(UNCHECKED)
	protected List<T> selecionarTodos(final String... camposOrdenacao) {
		return (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Criteria criteria = session
								.createCriteria(getClasseEntidade());
						for (String campo : camposOrdenacao) {
							criteria.addOrder(Order.asc(campo));
						}
						criteria.setCacheable(true);

						return criteria.list();
					}
				});
	}

	/**
	 * Seleciona as entidades que possuem atributos que correspondam aos
	 * atributos setados no entidade exemplo. Valores nulos no exemplo são
	 * ignorados na consulta e não é feita nenhuma ordenação no resultado.
	 * 
	 * @param entidadeExemplo
	 *            Entidade contendo os parâmetros da consulta.
	 * @return as entidades que correspondam aos parâmetros da consulta.
	 */
	@SuppressWarnings(UNCHECKED)
	protected List<T> selecionarVarios(final T entidadeExemplo) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.createCriteria(getClasseEntidade()).add(
						Example.create(entidadeExemplo)).list();
			}
		});
	}
	
	/**
	 * Seleciona as entidades que possuem atributos que correspondam aos
	 * atributos setados no entidade exemplo. Valores nulos no exemplo são
	 * ignorados na consulta e não é feita nenhuma ordenação no resultado.
	 * 
	 * @param entidadeExemplo
	 *            Entidade contendo os parâmetros da consulta.
	 * @return as entidades que correspondam aos parâmetros da consulta.
	 */
	@SuppressWarnings(UNCHECKED)
	protected T selecionar(final T entidadeExemplo) {
		List<T> lista = selecionarVarios(entidadeExemplo);
		return (lista == null || lista.isEmpty()) ? null : lista.get(0);
	}
}
