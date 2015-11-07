package com.weektodo.db;


public class DefaultChainItemBuilder extends AbstractChainItemBuilder<DefaultChainItem> {

	@Override
	protected DefaultChainItem buildChainItemDataTarget() {
		return new DefaultChainItem();
	}

}
