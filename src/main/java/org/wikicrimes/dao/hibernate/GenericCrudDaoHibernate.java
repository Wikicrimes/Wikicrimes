package org.wikicrimes.dao.hibernate;

import java.util.List;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.wikicrimes.dao.GenericCrudDao;
import org.wikicrimes.model.BaseObject;

public abstract class GenericCrudDaoHibernate extends HibernateDaoSupport implements
		GenericCrudDao {

	private Class entity = BaseObject.class;

	public boolean save(BaseObject bo) {
		try {
			
			getHibernateTemplate().saveOrUpdate(bo);

			if (logger.isDebugEnabled()) {
				logger.debug("BO set to: " + bo);
			}
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean delete(BaseObject to) {
		try {
			getHibernateTemplate().delete(to);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public BaseObject get(Long id) {
		BaseObject bo = (BaseObject) getHibernateTemplate().get(entity, id);
		if (bo == null) {
			throw new ObjectRetrievalFailureException(entity, id);
		}
		return bo;
	}

	public List<BaseObject> getAll() {
		String nameEntity = entity.getName();
		return getHibernateTemplate().find("from " + nameEntity);
	}
	
	public List<BaseObject> find(BaseObject bo) {
		return getHibernateTemplate().findByExample(bo);
	}

	public boolean exist(BaseObject bo) {
		List list = getHibernateTemplate().findByExample(bo);
		
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Class getEntity() {
		return entity;
	}

	public void setEntity(Class entity) {
		this.entity = entity;
	}

}
