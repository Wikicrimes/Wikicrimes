package org.wikicrimes.model;

import java.util.Date;

public class MobileRequestLog extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date date;
	private String userAgent;

	public MobileRequestLog(Date date, String userAgent) {
		super();
		this.date = date;
		this.userAgent = userAgent;
	}
	
	public MobileRequestLog() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
