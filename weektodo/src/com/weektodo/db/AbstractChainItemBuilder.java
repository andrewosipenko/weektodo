package com.weektodo.db;

import android.database.Cursor;

public abstract class AbstractChainItemBuilder<T extends UpdatableChainItem> implements ChainItemBuilder<T> {

	public T build(Cursor c) {
		T res = buildChainItemDataTarget();
		res.setDbId(c.getLong(0));
		res.setPrevId(getPrevId(c));
		return res;
	}
	protected abstract T buildChainItemDataTarget();
	
	private Long getPrevId(Cursor c){
		if(!c.isNull(1)){
			return c.getLong(1);
		}
		return null;
	}
}
