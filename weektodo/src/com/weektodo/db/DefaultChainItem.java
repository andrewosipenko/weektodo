package com.weektodo.db;

public class DefaultChainItem implements UpdatableChainItem {
	private Long dbId;
	private Long prevId;
	

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	public Long getDbId() {
		return dbId;
	}

	public Long getPrevId() {
		return prevId;
	}
}
