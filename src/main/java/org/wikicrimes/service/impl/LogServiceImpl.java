package org.wikicrimes.service.impl;

import org.wikicrimes.dao.MobileRequestLogDao;
import org.wikicrimes.model.MobileRequestLog;
import org.wikicrimes.service.LogService;

public class LogServiceImpl extends GenericCrudServiceImpl implements LogService {
	private MobileRequestLogDao mobileRequestLogDao;
	@Override
	public boolean save(MobileRequestLog log) {
		return mobileRequestLogDao.save(log);
	}
	
	public MobileRequestLogDao getMobileRequestLogDao() {
		return mobileRequestLogDao;
	}
	public void setMobileRequestLogDao(MobileRequestLogDao mobileRequestLogDao) {
		this.mobileRequestLogDao = mobileRequestLogDao;
	}
		
	
}
