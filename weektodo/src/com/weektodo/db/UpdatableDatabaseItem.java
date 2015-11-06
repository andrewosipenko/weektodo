package com.weektodo.db;

public interface UpdatableDatabaseItem extends DatabaseItem, UpdatableChainItem {
	void setDescription(String description);
	void setCrossed(boolean crossed);
}
