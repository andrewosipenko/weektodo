package com.weektodo.db;

import android.database.Cursor;

public interface ChainItemBuilder<T extends ChainItem> {
	T build(Cursor c);
}
