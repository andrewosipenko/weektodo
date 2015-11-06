package com.weektodo.db;

public interface DatabaseItem extends ChainItem {
	CharSequence getDescription();
	boolean isCrossed();
	
}
