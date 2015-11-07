package com.weektodo.ui;

import com.weektodo.MainActivity;
import com.weektodo.db.AbstractDatabaseItemBuilder;

public class TodoItemGroupSeparatorBuilder extends AbstractDatabaseItemBuilder{
	private MainActivity context;
	
	public TodoItemGroupSeparatorBuilder(MainActivity context) {
		this.context = context;
	}

	@Override
	protected TodoItemGroupSeparator buildChainItemDataTarget() {
		return new TodoItemGroupSeparator(context);
	}

}
