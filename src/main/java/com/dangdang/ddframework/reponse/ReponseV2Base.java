package com.dangdang.ddframework.reponse;

import java.util.Date;

public class ReponseV2Base {
	protected Status status;
	protected Date systemDate;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}
}
