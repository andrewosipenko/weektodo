package com.weektodo.db;

public interface UpdatableChainItem extends ChainItem {
	void setDbId(Long id);
	void setPrevId(Long prevId);
}
