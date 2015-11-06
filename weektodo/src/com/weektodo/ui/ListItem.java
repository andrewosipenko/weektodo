package com.weektodo.ui;

import com.weektodo.db.UpdatableDatabaseItem;

public interface ListItem extends UpdatableDatabaseItem {
	int getColor();
	void setColor(int color);
}
