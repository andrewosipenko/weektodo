package com.weektodo.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

public class CrossedItemRemover {
	private ChainItemBuilder<?> chainItemBuilder;
	private SQLiteDatabase db;
	private ItemLoader itemLoader;

	public CrossedItemRemover(ChainItemBuilder<?> chainItemBuilder,
			SQLiteDatabase db, ItemLoader itemLoader) {
		this.chainItemBuilder = chainItemBuilder;
		this.db = db;
		this.itemLoader = itemLoader;
	}

	public void removeCrossedItems() {
		List<? extends ChainItem> chainItems = itemLoader.loadList("crossed=1",
				chainItemBuilder);

		Map<Long, ChainItem> crossedChainMap = buildChainMap(chainItems);
		for (Map.Entry<Long, ChainItem> entry : crossedChainMap.entrySet()) {
			itemLoader.updatePrevId("prevId=" + entry.getKey(), entry
					.getValue().getPrevId());
		}
		db.delete("TodoItem", "crossed=1", null);
	}

	Map<Long, ChainItem> buildChainMap(List<? extends ChainItem> chainItems) {
		Map<Long, ChainItem> crossedChainMap = new HashMap<Long, ChainItem>();// key:
																				// db
																				// id,
																				// value:
																				// last
																				// chaiItem
		for (ChainItem chainItem : chainItems) {
			crossedChainMap.put(chainItem.getDbId(), chainItem);
		}
		boolean changed;
		do {
			changed = false;
			for(Map.Entry<Long, ChainItem> entry : crossedChainMap.entrySet()){
				ChainItem tail = crossedChainMap.remove(entry.getValue().getPrevId());
				if (tail != null) {
					crossedChainMap.put(entry.getKey(), tail);
					changed = true;
					break;
				}
			}
		} while (changed);		
		return crossedChainMap;
	}

}
