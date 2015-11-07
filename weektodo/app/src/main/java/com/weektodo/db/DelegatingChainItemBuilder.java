package com.weektodo.db;

import android.database.Cursor;

public class DelegatingChainItemBuilder implements ChainItemBuilder<UpdatableDatabaseItem>{
	public static final String TYPE_ITEM = "item";
	public static final String TYPE_SEPARATOR = "separator";
	
	private AbstractDatabaseItemBuilder todoItemBuilder;
	private AbstractDatabaseItemBuilder todoItemGroupSeparatorBuilder;
	
	
	public DelegatingChainItemBuilder(
			AbstractDatabaseItemBuilder todoItemBuilder,
			AbstractDatabaseItemBuilder todoItemGroupSeparatorBuilder) {
		this.todoItemBuilder = todoItemBuilder;
		this.todoItemGroupSeparatorBuilder = todoItemGroupSeparatorBuilder;
	}


	public UpdatableDatabaseItem build(Cursor c) {
		String type = c.getString(3);
		UpdatableDatabaseItem res;
		if(TYPE_ITEM.equals(type)){
			res = todoItemBuilder.build(c);
		}
		else if(TYPE_SEPARATOR.equals(type)){
			res = todoItemGroupSeparatorBuilder.build(c);
		}
		else{
			throw new IllegalArgumentException("Unsupported type:" + type);
		}
		return res;
	}

}
