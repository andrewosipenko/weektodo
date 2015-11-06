package com.weektodo.db;

import android.database.Cursor;

public abstract class AbstractDatabaseItemBuilder extends AbstractChainItemBuilder<UpdatableDatabaseItem> {

	@Override
	public UpdatableDatabaseItem build(Cursor c) {
		UpdatableDatabaseItem res = super.build(c);
		res.setDescription(c.getString(2));
		Integer crossed = c.getInt(4); 
		res.setCrossed(crossed != null && crossed.equals(1));
		return res;
	}

}
